package cz.cvut.fit.sp1.githubreports.model.project;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Commit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commitId;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private String hashHubId;

    @Column(nullable = false)
    private String loginAuthor;

    @Column(nullable = false)
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
