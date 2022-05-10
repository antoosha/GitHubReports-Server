package cz.cvut.fit.sp1.githubreports.api.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryDTO {

    private Long repositoryID;
    private String repositoryName;
    private Long projectID;
    private List<Long> commitsIDs;

}
