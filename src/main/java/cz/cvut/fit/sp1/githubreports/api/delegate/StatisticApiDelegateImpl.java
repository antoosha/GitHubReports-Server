package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticMapper;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticSPI;
import lombok.AllArgsConstructor;
import org.openapi.api.StatisticsApi;
import org.openapi.model.StatisticDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@AllArgsConstructor
public class StatisticApiDelegateImpl implements StatisticsApi {

    private final StatisticSPI statisticSPI;

    private final StatisticMapper statisticMapper;

    @Override
    public ResponseEntity<StatisticDTO> getStatistic(Long id) {
        return ResponseEntity.ok
                (statisticMapper.toDTO(statisticSPI.readById(id)));
    }

    @Override
    public ResponseEntity<List<StatisticDTO>> getStatistics() {
        return ResponseEntity.ok
                (statisticMapper.toDTOs(statisticSPI.readAll().stream().toList()));
    }

    @Override
    public ResponseEntity<Void> deleteStatistic(Long id) {
        try {
            statisticSPI.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
