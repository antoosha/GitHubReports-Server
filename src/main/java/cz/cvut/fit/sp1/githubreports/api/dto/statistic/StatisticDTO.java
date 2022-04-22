package cz.cvut.fit.sp1.githubreports.api.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {
    private Long statisticId;
    private LocalDateTime createdDate;
    private String statisticTypeName;
    private Long authorID;
    private Long projectID;
    private String pathToFileWithGeneratedStat;
}
