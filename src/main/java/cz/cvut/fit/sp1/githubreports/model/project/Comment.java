package cz.cvut.fit.sp1.githubreports.model.project;

import com.sun.istack.NotNull;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private String text;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "commitId", nullable = false)
    private Commit commit;

}
