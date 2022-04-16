package cz.cvut.fit.sp1.githubreports.api.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

    private Long tagID;
    private String tagName;
    private Long projectID;
    private List<Long> commitsIDs;

}
