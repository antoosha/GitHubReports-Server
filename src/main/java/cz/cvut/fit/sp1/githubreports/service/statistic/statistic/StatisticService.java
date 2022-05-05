package cz.cvut.fit.sp1.githubreports.service.statistic.statistic;

import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
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
    public Collection<Statistic> readAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Statistic> readById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Statistic create(Statistic statistic) throws EntityStateException {
        if (statistic.getStatisticId() != null && repository.existsById(statistic.getStatisticId())) {
            throw new EntityStateException();
        }
        return repository.save(statistic);
    }

    @Override
    public Statistic update(Long id, Statistic statistic) throws EntityStateException {
        if (!repository.existsById(id)) {
            throw new EntityStateException();
        }
        return repository.save(statistic);
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
