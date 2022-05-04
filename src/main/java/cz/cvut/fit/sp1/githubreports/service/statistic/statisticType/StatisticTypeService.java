package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
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
    public Collection<StatisticType> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<StatisticType> readById(String id) {
        return repository.findById(id);
    }

    @Override
    public StatisticType create(StatisticType statisticType) throws EntityStateException {
        if (repository.existsById(statisticType.getStatisticName())) {
            throw new EntityStateException();
        }
        return repository.save(statisticType);
    }

    @Override
    public StatisticType update(String id, StatisticType statisticType) throws EntityStateException {
        if (!repository.existsById(id)) {
            throw new EntityStateException();
        }
        return repository.save(statisticType);
    }

    @Override
    public void delete(String id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
