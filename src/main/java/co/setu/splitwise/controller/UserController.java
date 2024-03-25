package co.setu.splitwise.controller;

import co.setu.splitwise.dto.group.MemberDto;
import co.setu.splitwise.model.RegisteredUser;
import co.setu.splitwise.repository.UserRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

import static co.setu.splitwise.util.Util.failedJsonResponse;
import static co.setu.splitwise.util.Util.jsonResponse;


@Api(tags = "Splitwise User API", consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;


    @ApiIgnore
    @GetMapping("/user/get-group/{userId}")
    public ResponseEntity getGroups(@PathVariable String userId) {
        RegisteredUser user = userRepository.getById(userId);
        return jsonResponse(
                "userId", user.getUserId(),
                "groups", user.getGroup());
    }

    @GetMapping("/user/all")
    public ResponseEntity getAllRegisteredUsers() {
        List<RegisteredUser> registeredUserList = userRepository.findAll();
        if(registeredUserList != null && registeredUserList.size() > 0) {
            List<MemberDto> userList = registeredUserList.stream().map(user -> MemberDto.builder()
                    .userName(user.getUserName())
                    .userId(user.getUserId())
                    .mobile(user.getMobile())
                    .registeredAt(user.getRegisteredAt().toString())
                    .build()
            ).collect(Collectors.toList());

            return jsonResponse("users", userList);
        }
        else {
            return failedJsonResponse("No users registered found");
        }
    }
}
