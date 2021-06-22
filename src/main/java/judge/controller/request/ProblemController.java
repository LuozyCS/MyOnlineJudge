package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Comment;
import judge.mapper.ProblemMapper;
import judge.mapper.CommentMapper;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProblemController
{

    @Autowired private UserMapper userMapper;
    @Autowired private ProblemMapper problemMapper;
    @Autowired private CommentMapper commentMapper;
    @Autowired private CookieCheck cookieCheck;
    @GetMapping("/problem/id={id}")
    public String problem(
            @PathVariable("id") int id
            , Model model
            , HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        model=cookieCheck.check(cookies,model);
//        System.out.println(((User)model.getAttribute("User")).getId());
//        System.out.println("problem controller " + id);
        model.addAttribute("ProblemContent", problemMapper.getContent(id));
        model.addAttribute("ProblemTitle", problemMapper.getTitle(id));
        model.addAttribute("ProblemId",id);
        List<Comment> directComments = commentMapper.getDirectComments(id);
        model.addAttribute("DirectComments", directComments);
        for (Comment comment : directComments)
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));

//        System.err.println(model.getAttribute("Comments"));
        return "site/problem-details";
    }

    @RequestMapping("/list")
    public String pagelist(
            HttpServletRequest request
            ,Model model
    ){
        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就隐藏按钮
            model.addAttribute("failed","you are not administrator");
        }
        //在此添加排行榜内容



        return "admin/page_list";
    }
}
