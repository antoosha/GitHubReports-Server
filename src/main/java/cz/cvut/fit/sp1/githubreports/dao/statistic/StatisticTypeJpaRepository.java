package cz.cvut.fit.sp1.githubreports.dao.statistic;

import cz.cvut.fit.sp1.githubreports.model.statistic.StatisticType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticTypeJpaRepository extends JpaRepository<StatisticType, String> {
}
