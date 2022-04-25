package cz.cvut.fit.sp1.githubreports.dao.statistic;


import cz.cvut.fit.sp1.githubreports.model.statistic.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticJpaRepository extends JpaRepository<Statistic, Long> {
}
