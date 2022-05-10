package cz.cvut.fit.sp1.githubreports.api.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String username;
    private String email;
    private String password;
    private String pathToFileWithPhoto;
    private List<Long> commentsIDs = new ArrayList<>();
    private List<Long> projectsIDs = new ArrayList<>();
    private List<Long> createdProjectsIDs = new ArrayList<>();
    private List<Long> statisticsIDs = new ArrayList<>();
    private List<String> rolesIDs = new ArrayList<>();
    
}
