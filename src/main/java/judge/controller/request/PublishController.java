package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Article;
import judge.dataTransferObject.User;
import judge.mapper.ArticleMapper;
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
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CookieCheck cookieCheck;
    @PostMapping("/addProblem")
    public String addArticle(
            Article article
            , HttpServletRequest request
            , Model model
    ){
        Cookie[] cookies = request.getCookies();
        cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");
        article.setPublisher(user);

        String ErrorMessages=article.getErrorMessages();
        if (ErrorMessages==null)
            articleMapper.insertArticle(article);
        else
            model.addAttribute("failed",ErrorMessages);

        return "redirect:/admin_problem_list";
    }

    @PostMapping("/updateProblem")
    public String updateArticle(
            Article article
            , HttpServletRequest request
            , Model model
    ){

        User user=(User)model.getAttribute("User");

        article.setPublisher(user);

        String ErrorMessages=article.getErrorMessages();
        if( ErrorMessages==null || (ErrorMessages.isEmpty()) )
            articleMapper.updateArticle(article);
        else
            model.addAttribute("failed",ErrorMessages);


        return "redirect:/admin_problem_list";
    }

    @RequestMapping("/publish/problem_id = {id}")
    public String modifyArticle(
            @PathVariable("id") final int id
            , Model model
    )
    {

        model.addAttribute("oldcontent",articleMapper.getContent(id));
        model.addAttribute("oldid",id);
        return "admin/publish";
    }
}
