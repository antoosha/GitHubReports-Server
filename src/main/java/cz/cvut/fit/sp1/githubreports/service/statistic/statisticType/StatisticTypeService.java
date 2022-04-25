package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.dao.statistic.StatisticTypeJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
@Service("StatisticTypeService")
public class StatisticTypeService implements StatisticTypeSPI {

    private final StatisticTypeJpaRepository repository;


    @Override
    public Collection<StatisticType> readStatisticTypes() {
        return repository.findAll();
    }

    @Override
    public Optional<StatisticType> readById(String id) {
        return repository.findById(id);
    }

    @Override
    public void createStatistic(StatisticType statisticType) {
        repository.save(statisticType);
    }

    @Override
    public void updateStatistic(String id, StatisticType statisticType) {
        if (repository.existsById(id))
            repository.save(statisticType);
    }

    @Override
    public void deleteStatistic(String id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
