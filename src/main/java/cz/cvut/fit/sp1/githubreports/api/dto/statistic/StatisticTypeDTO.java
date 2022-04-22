package cz.cvut.fit.sp1.githubreports.api.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticTypeDTO {

    private String statisticName;
    private List<Long> statisticsIDs = new ArrayList<>();
}
