package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;


import cz.cvut.fit.sp1.githubreports.api.dto.statistic.StatisticTypeDTO;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class StatisticTypeConverter {

    private final StatisticService statisticService;

    public StatisticType toModel(StatisticTypeDTO statisticTypeDTO) {
        return new StatisticType(
                statisticTypeDTO.getStatisticName(),
                statisticTypeDTO.getStatisticsIDs().stream().
                        map(userId -> statisticService.readById(userId).orElseThrow(RuntimeException::new))
                        .collect(Collectors.toList())
        );
    }

    public StatisticTypeDTO fromModel(StatisticType statisticType) {
        return new StatisticTypeDTO(
                statisticType.getStatisticName(),
                statisticType.getStatistics().stream().map(Statistic::getStatisticId).collect(Collectors.toList())
        );
    }

    public Collection<StatisticType> toModelsMany(Collection<StatisticTypeDTO> statisticTypeDTOS) {
        return statisticTypeDTOS.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<StatisticTypeDTO> fromModelsMany(Collection<StatisticType> statisticTypes) {
        return statisticTypes.stream().map(this::fromModel).collect(Collectors.toList());
    }
}
