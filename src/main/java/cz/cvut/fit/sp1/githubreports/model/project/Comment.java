package cz.cvut.fit.sp1.githubreports.model.project;

import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    @Type(type = "text")
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;


    private String authorUsername;

    @ManyToOne
    @JoinColumn(name = "commit_id", nullable = false)
    private Commit commit;

    private Boolean isEdited; // TODO change logic

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return commentId.equals(comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", text='" + text + '\'' +
                ", createdDate=" + createdDate +
                ", authorUsername='" + authorUsername + '\'' +
                ", commitID=" + commit.getCommitId() +
                '}';
    }
}
