package cz.cvut.fit.sp1.githubreports.api.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ProjectDTO {

    private Long projectID;
    private LocalDateTime createdDate;
    private String projectName;
    private String description;
    private Long authorID;
    private List<Long> repositoriesIDs;
    private List<Long> statisticsIDs;
    private List<Long> usersIDs;
    private List<Long> tagsIDs;

}
