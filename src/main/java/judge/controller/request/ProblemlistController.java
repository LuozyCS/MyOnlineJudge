package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Problem;
import judge.dataTransferObject.User;
import judge.mapper.ProblemMapper;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ProblemlistController {
    @Autowired private UserMapper userMapper;
    @Autowired private ProblemMapper problemMapper;
    @Autowired private CookieCheck cookieCheck;

    @GetMapping("/admin_problem_list")
    public String problemlist(Model model,
                              HttpServletResponse response,
                              HttpServletRequest request
                              )
    {

        //确认cookie
        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }
//        cookieCheck.check(cookies,model);
//        User user=(User) model.getAttribute("User");
//        //通过cookie查找publisherId，查找文章
//        int tmpPublisherId=user.getId();

        //管理员可以看到所有题目
        List<Problem> userProblemList = problemMapper.getAllExceptContent();
        model.addAttribute("Problems", userProblemList);

        for(Problem a: userProblemList){
            System.out.println(a.getTitle());
        }

        return "admin/admin_problem_list";

    }

    //用于删除文章的controller
    @GetMapping("/delete_request/id={id}") public String deleteProblem(
            @PathVariable("id") int id,
            Model model,
            HttpServletResponse response,
            HttpServletRequest request)
    {

        System.out.println("delete controller");
        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }
        cookieCheck.check(cookies,model);
        User user=(User) model.getAttribute("User");

    if(user.getId()== problemMapper.getPublisher(id)) {
        problemMapper.deleteProblem(id);
    }
        return "redirect:/admin_problem_list";
    }


}



