package blog.controller.object;

import blog.mapper.UserMapper;
import blog.dataTransferObject.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController public class UserController
{
    @Autowired private UserMapper userMapper;//自动管理的是一样的

    @GetMapping("/username={username}") public User
        getUserByUsername(@PathVariable("username") final String username)
    {
        return userMapper.getUserByUsername(username);
    }

    @GetMapping("/email={email}") public User
        getUserByEmail(@PathVariable("email") final String email)
    {
        return userMapper.getUserByEmail(email);
    }
}
