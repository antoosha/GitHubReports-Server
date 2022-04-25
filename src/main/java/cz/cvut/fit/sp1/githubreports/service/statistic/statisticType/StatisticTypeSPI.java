package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;

import java.util.Collection;
import java.util.Optional;

public interface StatisticTypeSPI {
    Collection<StatisticType> readStatisticTypes();

    Optional<StatisticType> readById(String id);

    void createStatistic(StatisticType statisticType);

    void updateStatistic(String id, StatisticType statisticType);

    void deleteStatistic(String id);
}
