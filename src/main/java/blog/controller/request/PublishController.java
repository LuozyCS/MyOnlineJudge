package blog.controller.request;

import blog.controller.CookiendSession.CookieCheck;
import blog.dataTransferObject.Article;
import blog.dataTransferObject.User;
import blog.mapper.ArticleMapper;
import blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

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
    @PostMapping("/addArticle")
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

        return "redirect:/article";
    }

    @PostMapping("/updateArticle")
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


        return "redirect:/article";
    }

    @RequestMapping("/publish/article_id = {id}")
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
