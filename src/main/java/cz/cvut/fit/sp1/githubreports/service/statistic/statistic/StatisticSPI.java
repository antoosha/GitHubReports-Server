package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;

import java.util.Collection;
import java.util.Optional;

public interface StatisticSPI {
    Collection<Statistic> readAll();

    Optional<Statistic> readById(Long id);

    Statistic create(Statistic statistic) throws EntityStateException;

    Statistic update(Long id, Statistic statistic) throws EntityStateException;

    void delete(Long id);
}
