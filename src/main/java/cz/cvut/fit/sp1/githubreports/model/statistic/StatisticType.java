package cz.cvut.fit.sp1.githubreports.model.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticType {

    @Id
    private String statisticName;

    @OneToMany(mappedBy = "statisticId")
    private List<Statistic> statistics;
}
