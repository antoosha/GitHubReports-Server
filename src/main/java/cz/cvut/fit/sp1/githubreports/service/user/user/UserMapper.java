package cz.cvut.fit.sp1.githubreports.service.user.user;

import cz.cvut.fit.sp1.githubreports.model.user.User;
import org.mapstruct.Mapper;
import org.openapi.model.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    UserSlimDTO toSlimDTO(User user);

    User fromSlimDTO(UserSlimDTO userSlimDTO);

    List<UserDTO> toDTOs(List<User> users);

    List<UserSlimDTO> toSlimDTOs(List<User> users);

}
