package judge.controller.CookiendSession;

import judge.dataTransferObject.User;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;

@Configuration
public class CookieCheck {
    @Autowired
    private UserMapper userMapper;

    public Model check(Cookie[] cookies, Model model){
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie:cookies) {
//                System.out.println("cookie===for遍历"+cookie.getName());
//                log.debug("cookie===for遍历"+cookie.getName());
                if (StringUtils.equalsIgnoreCase(cookie.getName(), "isLogin")) {
                    User user=userMapper.getUserByUsername(cookie.getValue());
                    model.addAttribute("LoginName",cookie.getValue());
                    model.addAttribute("User",user);
                    break;
                }
            }
        }
        return model;
    }
}
