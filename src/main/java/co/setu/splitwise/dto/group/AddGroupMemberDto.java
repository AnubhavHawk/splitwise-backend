package co.setu.splitwise.dto.group;

import co.setu.splitwise.model.RegisteredUser;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AddGroupMemberDto {
    private String groupId;
    private List<String> groupMemberList;

    public List<RegisteredUser> mapUser() {
        return groupMemberList.stream().map(userId ->
                RegisteredUser.builder().userId(userId).build()).collect(Collectors.toList());
    }
}
