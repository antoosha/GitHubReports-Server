package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;

import java.util.List;

public interface StatisticSPI {
    List<Statistic> readAll();

    Statistic readById(Long id);

    Statistic create(Statistic statistic) throws EntityStateException;

    Statistic update(Long id, Statistic statistic) throws EntityStateException;

    void delete(Long id);
}
