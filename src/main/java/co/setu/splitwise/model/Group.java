package co.setu.splitwise.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "splitwise_group") // group is a reserved keyword in SQL, so we need to rename the table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    private String groupId;

    private String groupName;
    @OneToOne
    private RegisteredUser createdBy;
    @ManyToMany(/*mappedBy = "group"*/)
    private List<RegisteredUser> groupMembers;
}
