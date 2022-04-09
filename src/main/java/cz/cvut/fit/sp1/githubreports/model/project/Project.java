package cz.cvut.fit.sp1.githubreports.model.project;

import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private LocalDateTime createdDate;

    private String projectName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User author;

    @ManyToMany
    @JoinTable(
            name = "project_repository",
            joinColumns = {@JoinColumn(name = "projectId")},
            inverseJoinColumns = {@JoinColumn(name = "repositoryId")}
    )
    private List<Repository> repositories;

    @OneToMany(mappedBy = "statisticId")
    private List<Statistic> statistics;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = {@JoinColumn(name = "projectId")},
            inverseJoinColumns = {@JoinColumn(name = "userId")}
    )
    private List<User> users;

    @OneToMany(mappedBy = "tagId")
    private List<Tag> tags;

}
