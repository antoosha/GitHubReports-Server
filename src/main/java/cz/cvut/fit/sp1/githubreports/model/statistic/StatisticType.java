package cz.cvut.fit.sp1.githubreports.model.statistic;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticType {

    @Id
    private String statisticName;

    @OneToMany(mappedBy = "statisticId")
    private List<Statistic> statistics;
}
