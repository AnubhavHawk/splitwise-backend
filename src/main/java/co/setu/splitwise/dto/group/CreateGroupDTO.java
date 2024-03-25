package co.setu.splitwise.dto.group;

import co.setu.splitwise.model.Group;
import co.setu.splitwise.model.RegisteredUser;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static co.setu.splitwise.util.RandomIdGenerator.generateRandomId;

@Getter
public class CreateGroupDTO {
    private String groupName;
    private List<String> groupMemberIdList; // Only userId
    private String groupAdmin;

    public Group mapToNewGroup() {
        List userSet = new ArrayList();
        List<RegisteredUser> memberList = groupMemberIdList.stream()
                .map(id -> RegisteredUser.builder().userId(id).build())
                .collect(Collectors.toList());
        memberList.add(RegisteredUser.builder().userId(groupAdmin).build()); // Add admin also to the group
        userSet.addAll(memberList);

        return Group.builder()
                .groupId(generateRandomId())
                .groupName(groupName)
                .groupMembers(userSet)
                .createdBy(RegisteredUser.builder().userId(groupAdmin).build())
                .build();
    }
}
