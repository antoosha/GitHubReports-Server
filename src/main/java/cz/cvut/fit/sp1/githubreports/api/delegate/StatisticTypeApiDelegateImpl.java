package cz.cvut.fit.sp1.githubreports.api.delegate;

import cz.cvut.fit.sp1.githubreports.service.statistic.statisticType.StatisticTypeMapper;
import cz.cvut.fit.sp1.githubreports.service.statistic.statisticType.StatisticTypeSPI;
import lombok.AllArgsConstructor;
import org.openapi.api.StatisticTypesApi;
import org.openapi.model.StatisticTypeDTO;
import org.openapi.model.StatisticTypeSlimDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@AllArgsConstructor
public class StatisticTypeApiDelegateImpl implements StatisticTypesApi {

    private final StatisticTypeSPI statisticTypeSPI;

    private final StatisticTypeMapper statisticTypeMapper;
    @Override
    public ResponseEntity<StatisticTypeDTO> getStatisticType(String id) {
        return ResponseEntity.ok
                (statisticTypeMapper.toDTO(statisticTypeSPI.readById(id)));
    }

    @Override
    public ResponseEntity<List<StatisticTypeSlimDTO>> getStatisticTypes() {
        return ResponseEntity.ok
                (statisticTypeMapper.toSlimDTOs(statisticTypeSPI.readAll().stream().toList()));
    }

    @Override
    public ResponseEntity<StatisticTypeDTO> updateStatisticType(String id, StatisticTypeSlimDTO statisticTypeSlimDTO) {

        return  ResponseEntity.ok
                (statisticTypeMapper.toDTO(statisticTypeSPI.update
                        (id, statisticTypeMapper.fromSlimDTO(statisticTypeSlimDTO))));
    }

    @Override
    public ResponseEntity<Void> deleteStatisticType(String id) {
        try {
            statisticTypeSPI.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
