package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Problem;
import judge.dataTransferObject.User;
import judge.mapper.ProblemMapper;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@Component
public class PublishController {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CookieCheck cookieCheck;
    @PostMapping("/addProblem")
    public String addProblem(
            Problem problem
            , HttpServletRequest request
            , Model model
    ){
        Cookie[] cookies = request.getCookies();
        cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");
        problem.setPublisher(user);

        String ErrorMessages= problem.getErrorMessages();
        if (ErrorMessages==null)
            problemMapper.insertProblem(problem);
        else
            model.addAttribute("failed",ErrorMessages);

        return "redirect:/admin_problem_list";
    }

    @PostMapping("/updateProblem")
    public String updateProblem(
            Problem problem
            , HttpServletRequest request
            , Model model
    ){

        User user=(User)model.getAttribute("User");

        problem.setPublisher(user);

        String ErrorMessages= problem.getErrorMessages();
        if( ErrorMessages==null || (ErrorMessages.isEmpty()) )
            problemMapper.updateProblem(problem);
        else
            model.addAttribute("failed",ErrorMessages);


        return "redirect:/admin_problem_list";
    }

    @RequestMapping("/publish/problem_id = {id}")
    public String modifyProblem(
            @PathVariable("id") final int id
            , Model model
    )
    {

        model.addAttribute("oldcontent", problemMapper.getContent(id));
        model.addAttribute("oldid",id);
        return "admin/publish";
    }
}
