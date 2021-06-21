package judge.mapper;

import judge.dataTransferObject.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper @Repository
public interface UserMapper
{
    User getUserById(final int id);
    User getUserByUsername(final String username);
    User getUserByEmail(final String email);
    void insertUser(final User user);
}
