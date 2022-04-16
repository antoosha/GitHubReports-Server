package cz.cvut.fit.sp1.githubreports.api.dto.statistic;

import cz.cvut.fit.sp1.githubreports.model.project.Project;
import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;
import cz.cvut.fit.sp1.githubreports.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {
    private Long statisticId;
    private LocalDateTime createdDate;
    private StatisticType statisticType;
    private User authorID;
    private Project projectID;
    private String pathToFileWithGeneratedStat;
}
