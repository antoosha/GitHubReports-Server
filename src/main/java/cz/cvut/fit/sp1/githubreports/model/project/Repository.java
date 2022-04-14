package cz.cvut.fit.sp1.githubreports.model.project;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repositoryId;

    @Column(nullable = false)
    private String repositoryName;

    @OneToMany(mappedBy = "commitId")
    private List<Commit> commits;

    @ManyToMany(mappedBy = "repositories")
    private List<Project> projects;
}
