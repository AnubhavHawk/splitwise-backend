package co.setu.splitwise.service;

import co.setu.splitwise.model.Group;
import co.setu.splitwise.model.RegisteredUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GroupServiceTest {

    @Autowired
    private GroupService groupService;

    @Test
    public void testNewGroupCreation() {
        String expectedGroupName = "Test Group";
        Group created = groupService.createGroup(getDummyGroup(expectedGroupName));

        Assertions.assertEquals(expectedGroupName, created.getGroupName());
    }

    private Group getDummyGroup(String groupName) {

        RegisteredUser user1 = RegisteredUser.builder().userId("user-002").build();
        RegisteredUser user2 = RegisteredUser.builder().userId("user-003").build();
        List<RegisteredUser> memberList = new ArrayList();
        memberList.add(user1);
        memberList.add(user2);

        return Group.builder()
                .groupName(groupName)
                .createdBy(RegisteredUser.builder().userId("user-001").build())
                .groupMembers(memberList)
                .build();
    }
}
