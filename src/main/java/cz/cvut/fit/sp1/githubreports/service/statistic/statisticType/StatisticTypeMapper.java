package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapi.model.StatisticTypeDTO;
import org.openapi.model.StatisticTypeSlimDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StatisticTypeMapper {

    StatisticTypeDTO toDTO(StatisticType statisticType);

    StatisticTypeSlimDTO toSlimDTO(StatisticType statisticType);

    @Mapping(target = "statistics", ignore = true)
    StatisticType fromSlimDTO(StatisticTypeSlimDTO statisticTypeSlimDTO);

    List<StatisticTypeDTO> toDTOs(List<StatisticType> statisticTypes);

    List<StatisticTypeSlimDTO> toSlimDTOs(List<StatisticType> statisticTypes);
    
}
