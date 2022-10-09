package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import org.mapstruct.Mapper;
import org.openapi.model.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatisticMapper {

    StatisticDTO toDTO(Statistic statistic);

    StatisticSlimDTO toSlimDTO(Statistic statistic);

    Statistic fromSlimDTO(StatisticSlimDTO statisticSlimDTO);

    List<StatisticDTO> toDTOs(List<Statistic> statistics);

    List<StatisticSlimDTO> toSlimDTOs(List<Statistic> statistics);
    
}
