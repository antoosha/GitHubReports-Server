package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;

import java.util.Collection;
import java.util.Optional;

public interface StatisticSPI {
    Collection<Statistic> readStatistics();

    Optional<Statistic> readById(Long id);

    void createStatistic(Statistic statistic);

    void updateStatistic(Long id, Statistic statistic);

    void deleteStatistic(Long id);
}
