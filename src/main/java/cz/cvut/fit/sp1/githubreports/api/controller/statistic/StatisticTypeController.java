package cz.cvut.fit.sp1.githubreports.api.controller.statistic;

import cz.cvut.fit.sp1.githubreports.api.dto.statistic.StatisticTypeDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.statistic.statisticType.StatisticTypeConverter;
import cz.cvut.fit.sp1.githubreports.service.statistic.statisticType.StatisticTypeSPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RestController
@RequestMapping("/statisticTypes")
public class StatisticTypeController {

    private final StatisticTypeSPI statisticTypeSPI;
    private final StatisticTypeConverter statisticTypeConverter;

    @GetMapping()
    public Collection<StatisticTypeDTO> getAll() {
        return statisticTypeConverter.fromModelsMany(statisticTypeSPI.readAll());
    }

    @GetMapping("/{id}")
    public StatisticTypeDTO getOne(@PathVariable String id) {
        return statisticTypeConverter.fromModel(statisticTypeSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public StatisticTypeDTO create(@RequestBody StatisticTypeDTO statisticTypeDTO) throws EntityStateException {
        return statisticTypeConverter.fromModel(statisticTypeSPI.create(statisticTypeConverter.toModel(statisticTypeDTO)));
    }

    @PutMapping("/{id}")
    public StatisticTypeDTO update(@PathVariable String id, @RequestBody StatisticTypeDTO statisticTypeDTO) throws IncorrectRequestException, EntityStateException {
        if (!statisticTypeDTO.getStatisticName().equals(id)) {
            throw new IncorrectRequestException();
        }
        return statisticTypeConverter.fromModel(statisticTypeSPI.update(statisticTypeDTO.getStatisticName(), statisticTypeConverter.toModel(statisticTypeDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        if (statisticTypeSPI.readById(id).isEmpty()) {
            throw new NoEntityFoundException();
        }
        statisticTypeSPI.delete(id);
    }


}
