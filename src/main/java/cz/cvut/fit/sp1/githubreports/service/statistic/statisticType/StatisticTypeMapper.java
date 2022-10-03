package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.StatisticTypeDTO;

@Mapper
public interface StatisticTypeMapper {

    @Mapping(source = "statistics", target = "statisticsIds")
    StatisticTypeDTO fromModel(StatisticType statisticType);

    @Mapping(target = "statistics", source = "statisticsIds")
    StatisticType toModel(StatisticTypeDTO statisticTypeDTO);

}
