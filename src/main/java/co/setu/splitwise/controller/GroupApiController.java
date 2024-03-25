package co.setu.splitwise.controller;

import co.setu.splitwise.dto.group.AddGroupMemberDto;
import co.setu.splitwise.dto.group.CreateGroupDTO;
import co.setu.splitwise.dto.group.MemberDto;
import co.setu.splitwise.model.Group;
import co.setu.splitwise.model.RegisteredUser;
import co.setu.splitwise.repository.UserRepository;
import co.setu.splitwise.service.GroupService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static co.setu.splitwise.util.Util.failedJsonResponse;
import static co.setu.splitwise.util.Util.jsonResponse;

@Api(tags = "Splitwise Group API", consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class GroupApiController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private GroupService groupService;

    private static final Logger logger = LoggerFactory.getLogger(GroupApiController.class);

    @PostMapping(value = "/group/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createGroup(@RequestBody CreateGroupDTO createGroupDTO) {
        try {
            Group created = groupService.createGroup(map(createGroupDTO));
            return jsonResponse(
                    "groupId", created.getGroupId(),
                    "createdBy", created.getCreatedBy().getUserName(),
                    "memberCount", created.getGroupMembers().size(),
                    "groupName", created.getGroupName());
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return failedJsonResponse(ex.getMessage());
        }
    }
    private Group map(CreateGroupDTO createGroupDTO) {
        RegisteredUser createdBy = RegisteredUser.builder().userId(createGroupDTO.getGroupAdmin()).build();
        List<RegisteredUser> members = new ArrayList<>();
        for(String memeberId: createGroupDTO.getGroupMemberIdList()) {
            members.add(RegisteredUser.builder().userId(memeberId).build());
        }
        return Group.builder()
                .groupName(createGroupDTO.getGroupName())
                .createdBy(createdBy)
                .groupMembers(members)
                .build();
    }

    @PostMapping("/group/add-member")
    public ResponseEntity addGroupMember(@RequestBody AddGroupMemberDto addGroupMemberDto) { // BUG: It only stores the new member and removes the existing group members
        try {
            Group updated = groupService.addMember(addGroupMemberDto.getGroupId(), addGroupMemberDto.mapUser());
            return jsonResponse(
                    "groupId", updated.getGroupId(),
                    "memberCount", updated.getGroupMembers().size(),
                    "createdBy", updated.getCreatedBy().getUserName());
        }
        catch (IllegalArgumentException e) {
            logger.error("Unable to add member to group {}", e.getMessage(), e);
            return failedJsonResponse(e.getMessage());
        }
    }

    @GetMapping("group/members/{groupId}")
    public ResponseEntity getGroupMembers(@PathVariable String groupId) {
        try {
            List<RegisteredUser> members = groupService.getGroupMembers(groupId);
            List<MemberDto> memberDto = members.stream().map(
                    member -> MemberDto.builder()
                                .registeredAt(member.getRegisteredAt().toString())
                                .userId(member.getUserId())
                                .userName(member.getUserName())
                                .mobile(member.getMobile())
                                .build()
            ).collect(Collectors.toList());

            return jsonResponse("groupId", groupId, "members", memberDto);
        }
        catch (EntityNotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return failedJsonResponse("Group " + groupId + " does not exist");
        }
    }
}
