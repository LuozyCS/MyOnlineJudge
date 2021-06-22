package judge.controller.request;

import judge.dataTransferObject.User;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AdminloginController {

    @Autowired private UserMapper userMapper;

    @PostMapping("/admin_login_request") public String login(
            @RequestParam(value = "username", required = false) final String username
            , @RequestParam("pwd") final String password
            , final HttpServletResponse response
            , final Model model) throws IOException {
        System.out.println("hello");
        System.out.printf("request to login: username = %s, password = %s\n", username, password);
        User user = userMapper.getUserByUsername(username) ;
        System.out.println(user);
        if (user == null)
        {
            model.addAttribute("failed", "no this person");
            return "/site/admin_login";
        } else if (!password.equals(user.getPwd()))
        {
            model.addAttribute("failed", "wrong password");
            return "/site/admin_login";
        }else if(user.getAdmin()==0){
            model.addAttribute("failed", "you are not administrator");
            return "/site/admin_login";
        }

        Cookie cookie1 = new Cookie("isLogin", user.getUsername());
        Cookie cookie2 = new Cookie("pwd",user.getPwd());
        cookie1.setPath("/");
        cookie2.setPath("/");
        response.addCookie(cookie1);
        response.addCookie(cookie2);


        return "redirect:list";
    }
}
