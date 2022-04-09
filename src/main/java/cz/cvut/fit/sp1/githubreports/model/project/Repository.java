package cz.cvut.fit.sp1.githubreports.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Repository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repositoryId;

    private String repositoryName;

    @OneToMany(mappedBy = "commitId")
    private List<Commit> commits;

    @ManyToMany(mappedBy = "repositories")
    private List<Project> projects;
}
