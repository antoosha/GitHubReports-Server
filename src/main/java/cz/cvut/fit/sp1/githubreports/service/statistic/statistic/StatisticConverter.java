package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.api.dto.statistic.StatisticDTO;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectService;
import cz.cvut.fit.sp1.githubreports.service.statistic.statisticType.StatisticTypeService;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class StatisticConverter {
    private final ProjectService projectService;
    private final StatisticTypeService statisticTypeService;
    private final UserService userService;


    public Statistic toModel(StatisticDTO statisticDTO) {
        return new Statistic(
                statisticDTO.getStatisticId(),
                statisticDTO.getCreatedDate(),
                statisticTypeService.readById(statisticDTO.getStatisticTypeName()).orElseThrow(RuntimeException::new),
                userService.readById(statisticDTO.getAuthorID()).orElseThrow(RuntimeException::new),
                projectService.readById(statisticDTO.getProjectID()).orElseThrow(RuntimeException::new),
                statisticDTO.getPathToFileWithGeneratedStat()
        );
    }

    public StatisticDTO fromModel(Statistic statistic) {
        return new StatisticDTO(
                statistic.getStatisticId(),
                statistic.getCreatedDate(),
                statistic.getStatisticType().getStatisticName(),
                statistic.getAuthor().getUserId(),
                statistic.getProject().getProjectId(),
                statistic.getPathToFileWithGeneratedStat()

        );
    }

    public Collection<Statistic> toModelsMany(Collection<StatisticDTO> statisticDTOS) {
        return statisticDTOS.stream().map(this::toModel).collect(Collectors.toList());
    }

    public Collection<StatisticDTO> fromModelsMany(Collection<Statistic> statistics) {
        return statistics.stream().map(this::fromModel).collect(Collectors.toList());
    }

}
