package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;

import java.util.Collection;
import java.util.Optional;

public interface StatisticSPI {
    Collection<Statistic> readAll();

    Optional<Statistic> readById(Long id);

    void create(Statistic statistic);

    void update(Long id, Statistic statistic);

    void delete(Long id);
}
