package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;

import java.util.Collection;
import java.util.Optional;

public interface StatisticTypeSPI {
    Collection<StatisticType> readAll();

    Optional<StatisticType> readById(String id);

    void create(StatisticType statisticType);

    void update(String id, StatisticType statisticType);

    void delete(String id);
}
