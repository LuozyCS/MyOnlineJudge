package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Problem;
import judge.mapper.ProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class HomepageController {
    @Autowired private ProblemMapper problemMapper;
    @Autowired private CookieCheck cookieCheck;

    @GetMapping({"/","/problem"})
    public String blog(Model model,
                       HttpServletResponse response,
                       HttpServletRequest request)
    {
        System.out.println(cookieCheck);
        Cookie[] cookies = request.getCookies();
        model=cookieCheck.check(cookies,model);

        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就隐藏右上角按钮
            model.addAttribute("failed","you are not administrator");
        }

        List<Problem> list=  problemMapper.getAllExceptContent();
        System.out.println(list);
        model.addAttribute("ProblemList",list);
        return "site/problem";
    }
//    System.getProperty("user.dir");
}
//import java.net.http.HttpClient.Redirect;