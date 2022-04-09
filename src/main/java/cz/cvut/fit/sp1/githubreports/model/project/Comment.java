package cz.cvut.fit.sp1.githubreports.model.project;

import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String text;

    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "commitId", nullable = false)
    private Commit commit;

}
