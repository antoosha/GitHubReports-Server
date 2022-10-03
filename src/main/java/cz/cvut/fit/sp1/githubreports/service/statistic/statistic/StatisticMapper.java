package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.StatisticDTO;

@Mapper
public interface StatisticMapper {

    @Mapping(source = "statisticType.statisticName", target = "statisticTypeName")
    @Mapping(source = "author.userId", target = "authorId")
    @Mapping(source = "project.projectId", target = "projectId")
    StatisticDTO fromModel(Statistic statistic);

    @Mapping(target = "statisticType.statisticName", source = "statisticTypeName")
    @Mapping(target = "author.userId", source = "authorId")
    @Mapping(target = "project.projectId", source = "projectId")
    Statistic toModel(StatisticDTO statisticDTO);
}
