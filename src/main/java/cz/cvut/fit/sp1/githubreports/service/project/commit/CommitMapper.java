package cz.cvut.fit.sp1.githubreports.service.project.commit;

import cz.cvut.fit.sp1.githubreports.model.project.Commit;
import org.mapstruct.Mapper;
import org.openapi.model.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommitMapper {

    CommitDTO toDTO(Commit commit);

    CommitSlimDTO toSlimDTO(Commit commit);

    Commit fromSlimDTO(CommitSlimDTO commitSlimDTO);

    List<CommitDTO> toDTOs(List<Commit> commits);

    List<CommitSlimDTO> toSlimDTOs(List<Commit> commits);

}
