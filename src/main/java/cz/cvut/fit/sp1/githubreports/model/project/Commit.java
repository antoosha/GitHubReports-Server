package cz.cvut.fit.sp1.githubreports.model.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commitId;

    private LocalDateTime createdDate;

    private String hashHubId;

    private String loginAuthor;

    private String description;

    @ManyToOne
    @JoinColumn(name = "repositoryId", nullable = false)
    private Repository repository;

    @ManyToMany
    @JoinTable(
            name = "commit_tag",
            joinColumns = {@JoinColumn(name = "commitId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")}
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "commentId")
    private List<Comment> comments;


}
