package cz.cvut.fit.sp1.githubreports.model.statistic;

import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statisticId;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "statistic_name", nullable = false)
    private StatisticType statisticType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String pathToFileWithGeneratedStat;
}
