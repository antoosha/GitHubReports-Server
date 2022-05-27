package cz.cvut.fit.sp1.githubreports.model.project;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    @Type(type = "text")
    private String description;

    @Column(nullable = false)
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "repository_id", nullable = false)
    private Repository repository;

    @ManyToMany
    @JoinTable(
            name = "commit_tag",
            joinColumns = {@JoinColumn(name = "commit_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "commit", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commit commit = (Commit) o;
        return commitId.equals(commit.commitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commitId);
    }

    @Override
    public String toString() {
        return "Commit{" +
                "deleted=" + isDeleted() +
                ", commitId=" + commitId +
                ", createdDate=" + createdDate +
                ", hashHubId='" + hashHubId + '\'' +
                ", loginAuthor='" + loginAuthor + '\'' +
                ", description='" + description + '\'' +
                ", isDeleted=" + isDeleted +
                ", repositoryID=" + repository.getRepositoryId() +
                ", tagsIDs=" + tags.stream().map(Tag::getTagId) +
                ", commentsIDs=" + comments.stream().map(Comment::getCommentId) +
                '}';
    }
}
