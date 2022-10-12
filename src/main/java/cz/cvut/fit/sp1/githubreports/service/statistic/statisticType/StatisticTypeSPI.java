package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;

import java.util.List;

public interface StatisticTypeSPI {
    List<StatisticType> readAll();

    StatisticType readById(String id);

    StatisticType create(StatisticType statisticType) throws EntityStateException;

    StatisticType update(String id, StatisticType statisticType) throws EntityStateException;

    void delete(String id);
}
