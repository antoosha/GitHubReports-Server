package cz.cvut.fit.sp1.githubreports.api.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CommentDTO {

    private Long commentID;
    private String text;
    private LocalDateTime createdDate;
    private Long authorID;
    private Long commitID;

}
