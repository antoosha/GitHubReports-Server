package cz.cvut.fit.sp1.githubreports.model.statistic;

import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistic statistic = (Statistic) o;
        return getStatisticId().equals(statistic.getStatisticId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatisticId());
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "statisticId=" + statisticId +
                ", createdDate=" + createdDate +
                ", statisticType=" + statisticType.getStatisticName() +
                ", authorUsername=" + author.getUsername() +
                ", projectID=" + project.getProjectId() +
                '}';
    }
}
