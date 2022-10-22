package cz.cvut.fit.sp1.githubreports.service.statistic.statisticType;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.dao.statistic.StatisticTypeJpaRepository;
import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@AllArgsConstructor
@Service("StatisticTypeService")
public class StatisticTypeService implements StatisticTypeSPI {

    private final StatisticTypeJpaRepository repository;

    private static final Logger logger = Logger.getLogger(StatisticTypeService.class.getName());

    @Override
    public Collection<StatisticType> readAll() {
        return repository.findAll();
    }

    @Override
    public StatisticType readById(String id) {
        return repository.findById(id).orElseThrow(
                () -> {
                    logger.warning("Can't find statistic type with id " + id);
                    return new NoEntityFoundException("Can't find statistic type with id " + id);
                });
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
            logger.warning("Statistic type with id " + id + " does not exist");
            throw new EntityStateException("Statistic type with id " + id + " does not exist");
        }
        return repository.save(statisticType);
    }

    @Override
    public void delete(String id) {
        if (repository.existsById(id))
            repository.deleteById(id);
        else {
            logger.warning("Can't find statistic type with id " + id);
            throw new NoEntityFoundException("Can't find statistic type with id " + id);
        }
    }
}
