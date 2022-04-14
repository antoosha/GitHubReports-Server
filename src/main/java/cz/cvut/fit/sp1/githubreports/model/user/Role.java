package cz.cvut.fit.sp1.githubreports.model.user;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
