package cz.cvut.fit.sp1.githubreports.model.project;

import lombok.*;
import javax.persistence.*;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        return repositoryId.equals(that.repositoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repositoryId);
    }

    @Override
    public String toString() {
        return "Repository{" +
                "repositoryId=" + repositoryId +
                ", repositoryName='" + repositoryName + '\'' +
                ", projects=" + projects +
                '}';
    }
}
