package cz.cvut.fit.sp1.githubreports.model.project;

import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private String projectName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToMany
    @JoinTable(
            name = "project_repository",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "repository_id")}
    )
    private List<Repository> repositories;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Statistic> statistics;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = {@JoinColumn(name = "project_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> users;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Tag> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return projectId.equals(project.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", createdDate=" + createdDate +
                ", projectName='" + projectName + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author +
                ", repositories=" + repositories +
                ", statistics=" + statistics +
                ", users=" + users +
                ", tags=" + tags +
                '}';
    }
}
