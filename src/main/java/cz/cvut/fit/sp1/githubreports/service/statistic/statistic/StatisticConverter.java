package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.api.dto.statistic.StatisticDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import cz.cvut.fit.sp1.githubreports.service.project.project.ProjectSPI;
import cz.cvut.fit.sp1.githubreports.service.statistic.statisticType.StatisticTypeSPI;
import cz.cvut.fit.sp1.githubreports.service.user.user.UserSPI;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class StatisticConverter {
    private final ProjectSPI projectSPI;
    private final StatisticTypeSPI statisticTypeSPI;
    private final UserSPI userSPI;


    public Statistic toModel(StatisticDTO statisticDTO) {
        return new Statistic(
                statisticDTO.getStatisticId(),
                statisticDTO.getCreatedDate(),
                statisticTypeSPI.readById(statisticDTO.getStatisticTypeName()).orElseThrow(IncorrectRequestException::new),
                userSPI.readById(statisticDTO.getAuthorID()).orElseThrow(IncorrectRequestException::new),
                projectSPI.readById(statisticDTO.getProjectID()).orElseThrow(IncorrectRequestException::new),
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
