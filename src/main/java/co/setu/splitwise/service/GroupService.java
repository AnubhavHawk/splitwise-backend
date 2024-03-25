package co.setu.splitwise.service;

import co.setu.splitwise.model.Group;
import co.setu.splitwise.model.RegisteredUser;
import co.setu.splitwise.repository.GroupRepository;
import co.setu.splitwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static co.setu.splitwise.util.RandomIdGenerator.generateRandomId;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    public Group createGroup(Group group) {
        group.setGroupId(generateRandomId());
        // Before saving checking if the users are registered.
        RegisteredUser creator = userRepository.findById(group.getCreatedBy().getUserId()).orElse(null);
        if(creator == null || creator.getUserId() == null) {
            throw new IllegalArgumentException("Group Admin "+ group.getCreatedBy().getUserId() +" is not registered");
        }

        group.getGroupMembers().add(creator); // Add the creator to the group also.
        return addMemberToGroup(group, group.getGroupMembers());
    }

    public Group addMember(String groupId, List<RegisteredUser> memberList) {
        int totalMemberCount = 0;
        Group group = groupRepository.getById(groupId);

        if(group == null) {
            throw new IllegalArgumentException("Group Id is not valid");
        }
        if(memberList == null) {
            throw new IllegalArgumentException("Members list can not be null");
        }
        else {
            memberList.addAll(group.getGroupMembers()); // Keep the existing members intact
            return addMemberToGroup(group, memberList);
        }
    }

    public Group addMemberToGroup(Group group, List<RegisteredUser> registeredUserList) {
        group.setGroupMembers(validateGroupMemberRegistration(registeredUserList));
        Group saved = groupRepository.save(group);
        return saved;
    }

    public List<RegisteredUser> getGroupMembers(String groupId) {
        Group group = groupRepository.getById(groupId);
        if(group != null) {
            return group.getGroupMembers();
        }
        else {
            throw new IllegalArgumentException("Group Id " + groupId + " is not valid");
        }
    }

    private void validateGroupMemberRegistration(Set<RegisteredUser> groupMembers) {
        List<RegisteredUser> membersIndDB =  userRepository.findAllById(groupMembers.stream()
                .map(RegisteredUser::getUserId)
                .collect(Collectors.toList()));
        if(membersIndDB.size() != groupMembers.size()) {
            groupMembers.removeAll(membersIndDB);
            throw new IllegalArgumentException("Some members are not registered: " + groupMembers);
        }
    }

    private List<RegisteredUser> validateGroupMemberRegistration(List<RegisteredUser> groupMembers) {
        boolean allRegistered = true;
        List<RegisteredUser> uniqueRegisteredUsers = new ArrayList<>();
        for(RegisteredUser registeredUser : groupMembers) {
            if(!uniqueRegisteredUsers.contains(registeredUser)) {
                uniqueRegisteredUsers.add(registeredUser);
            }
        }
        List<RegisteredUser> registeredRegisteredUsers = new ArrayList<>();
        for(RegisteredUser registeredUser : uniqueRegisteredUsers) {
            RegisteredUser fromDb = userRepository.findById(registeredUser.getUserId()).orElse(null);
            registeredRegisteredUsers.add(fromDb);

            if(fromDb == null) {
                allRegistered = false;
            }
        }
        if(allRegistered == false) {
            uniqueRegisteredUsers.removeAll(registeredRegisteredUsers);
            throw new IllegalArgumentException("Some members are not registered: " + uniqueRegisteredUsers.stream().map(RegisteredUser::getUserId).collect(Collectors.toList()));
        }
        return registeredRegisteredUsers;
    }
}