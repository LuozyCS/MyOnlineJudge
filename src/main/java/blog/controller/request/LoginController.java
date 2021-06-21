package blog.controller.request;

import blog.controller.CookiendSession.CookieCheck;
import blog.dataTransferObject.User;
import blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Controller public class LoginController
{
    @Autowired private UserMapper userMapper;

    @PostMapping("/login_request") public String login(
            @RequestParam(value = "username", required = false) final String username
            , @RequestParam(value = "email", required = false) final String email
            , @RequestParam("pwd") final String password
            , final HttpServletResponse response
            , final Model model) throws IOException {
        System.out.printf("request to login: username = %s, email = %s, password = %s\n", username, email, password);
        User user = email == null ?
                userMapper.getUserByUsername(username) : userMapper.getUserByEmail(email);
        System.out.println(user);
        if (user == null)
        {
            model.addAttribute("failed", "no this person");
            return "/site/login";
        } else if (!password.equals(user.getPwd()))
        {
            model.addAttribute("failed", "wrong password");
            return "/site/login";
        }
//        System.out.println(userMapper == new UserController().userMapper);//false
//        System.out.println(userMapper);//自动管理的是一样的
        Cookie cookie1 = new Cookie("isLogin", user.getUsername());
        Cookie cookie2 = new Cookie("pwd",user.getPwd());
        cookie1.setPath("/");
        cookie2.setPath("/");
        response.addCookie(cookie1);
        response.addCookie(cookie2);

        /////////////////////////////////////////////

        /////////////////////////////////////////////
        return "redirect:blog";
//        request.setAttribute("isLogin", username);
//        request.getSession().setAttribute("isLogin",username);
//        for (Cookie one : request.getCookies())
//            System.out.println(one.getName() + "  " + one.getValue());
    }
}
