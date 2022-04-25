package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.dao.statistic.StatisticJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;


@AllArgsConstructor
@Service("StatisticService")
public class StatisticService implements StatisticSPI {

    private final StatisticJpaRepository repository;


    @Override
    public Collection<Statistic> readStatistics() {
        return repository.findAll();
    }

    @Override
    public Optional<Statistic> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void createStatistic(Statistic statistic) {
        repository.save(statistic);
    }

    @Override
    public void updateStatistic(Long id, Statistic statistic) {
        if (repository.existsById(id))
            repository.save(statistic);
    }

    @Override
    public void deleteStatistic(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
