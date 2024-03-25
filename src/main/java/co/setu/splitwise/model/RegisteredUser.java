package co.setu.splitwise.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredUser implements Comparable {
    @Id
    private String userId;

    private String userName;
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registeredAt;
    private String mobile;
    @ManyToMany
    private List<Group> group;

//    @Override
//    public boolean equals(Object o) {
//        if (o == this) {
//            return true;
//        }
//        if (!(o instanceof RegisteredUser)) {
//            return false;
//        }
//        RegisteredUser user = (RegisteredUser) o;
//
//        return user.getUserId().equals(this.getUserId());
//    }

    @Override
    public int compareTo(Object o) {
        RegisteredUser casted = (RegisteredUser) o;
        return this.userId.compareTo(casted.getUserId());
    }
}