package cz.cvut.fit.sp1.githubreports.model.statistic;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticType {

    @Id
    private String statisticName;

    @OneToMany(mappedBy = "statisticType", cascade = CascadeType.ALL)
    private List<Statistic> statistics;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticType that = (StatisticType) o;
        return getStatisticName().equals(that.getStatisticName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatisticName());
    }

    @Override
    public String toString() {
        return "StatisticType{" +
                "statisticName='" + statisticName + '\'' +
                '}';
    }
}
