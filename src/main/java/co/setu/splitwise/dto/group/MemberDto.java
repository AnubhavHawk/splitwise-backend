package co.setu.splitwise.dto.group;

import lombok.*;

@Builder
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    private String userId;
    private String userName;
    private String registeredAt;
    private String mobile;
}
