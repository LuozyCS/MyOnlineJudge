package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Comment;
import judge.mapper.ArticleMapper;
import judge.mapper.CommentMapper;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ArticleController
{

    @Autowired private UserMapper userMapper;
    @Autowired private ArticleMapper articleMapper;
    @Autowired private CommentMapper commentMapper;
    @Autowired private CookieCheck cookieCheck;
    @GetMapping("/problem/id={id}")
    public String article(
            @PathVariable("id") int id
            , Model model
            , HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        model=cookieCheck.check(cookies,model);
//        System.out.println(((User)model.getAttribute("User")).getId());
//        System.out.println("article controller " + id);
        model.addAttribute("ProblemContent", articleMapper.getContent(id));
        model.addAttribute("ProblemTitle",articleMapper.getTitle(id));
        model.addAttribute("ProblemId",id);
        List<Comment> directComments = commentMapper.getDirectComments(id);
        model.addAttribute("DirectComments", directComments);
        for (Comment comment : directComments)
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));

//        System.err.println(model.getAttribute("Comments"));
        return "site/problem-details";
    }
}
