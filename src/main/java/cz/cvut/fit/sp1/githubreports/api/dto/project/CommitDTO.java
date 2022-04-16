package cz.cvut.fit.sp1.githubreports.api.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommitDTO {

    private Long commitId;
    private LocalDateTime createdDate;
    private String hashHubId;
    private String loginAuthor;
    private String description;
    private Long repositoryID;
    private List<Long> tagsID;
    private List<Long> commentsID;

}
