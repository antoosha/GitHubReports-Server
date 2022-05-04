package cz.cvut.fit.sp1.githubreports.api.controller.statistic;


import cz.cvut.fit.sp1.githubreports.api.dto.statistic.StatisticDTO;
import cz.cvut.fit.sp1.githubreports.api.exceptions.EntityStateException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.IncorrectRequestException;
import cz.cvut.fit.sp1.githubreports.api.exceptions.NoEntityFoundException;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticConverter;
import cz.cvut.fit.sp1.githubreports.service.statistic.statistic.StatisticSPI;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@AllArgsConstructor
@RestController
@RequestMapping("/statistics")
public class StatisticController {
    private final StatisticSPI statisticSPI;
    private final StatisticConverter statisticConverter;

    @GetMapping()
    public Collection<StatisticDTO> getAll() {
        return statisticConverter.fromModelsMany(statisticSPI.readAll());
    }

    @GetMapping("/{id}")
    public StatisticDTO getOne(@PathVariable Long id) {
        return statisticConverter.fromModel(statisticSPI.readById(id).orElseThrow(NoEntityFoundException::new));
    }

    @PostMapping()
    public StatisticDTO create(StatisticDTO statisticDTO) throws EntityStateException {
        return statisticConverter.fromModel(statisticSPI.create(statisticConverter.toModel(statisticDTO)));
    }

    @PutMapping("/{id}")
    public StatisticDTO update(@PathVariable Long id, @RequestBody StatisticDTO statisticDTO) throws IncorrectRequestException, EntityStateException {
        if (!statisticDTO.getStatisticId().equals(id)) {
            throw new IncorrectRequestException();
        }
        return statisticConverter.fromModel(statisticSPI.update(statisticDTO.getStatisticId(), statisticConverter.toModel(statisticDTO)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (statisticSPI.readById(id).isEmpty()) {
            throw new NoEntityFoundException();
        }
        statisticSPI.delete(id);
    }
}
