package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Article;
import judge.dataTransferObject.User;
import judge.mapper.ArticleMapper;
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
public class ArticlelistController {
    @Autowired private UserMapper userMapper;
    @Autowired private ArticleMapper articleMapper;
    @Autowired private CookieCheck cookieCheck;

    @GetMapping("/article")
    public String articlelist(Model model,
                              HttpServletResponse response,
                              HttpServletRequest request
                              )
    {


        //确认cookie
        Cookie[] cookies = request.getCookies();

        cookieCheck.check(cookies,model);
        User user=(User) model.getAttribute("User");
        //通过cookie查找publisherId，查找文章


        int tmpPublisherId=user.getId();
        List<Article> userArticleList=articleMapper.getAllExceptContent_ByPublisherId(tmpPublisherId);
        model.addAttribute("Articles",userArticleList);

        for(Article a: userArticleList){
            System.out.println(a.getTitle());
        }

        return "admin/article_list";



//        List<Article> list_0=  articleMapper.getAllExceptContent();
//        System.out.println(list_0);
//        model.addAttribute("ArticleList",list_0);


    }

    //用于删除文章的controller
    @GetMapping("/delete_request/id={id}") public String deleteArticle(
            @PathVariable("id") int id,
            Model model,
            HttpServletResponse response,
            HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        cookieCheck.check(cookies,model);
        User user=(User) model.getAttribute("User");

    if(user.getId()==articleMapper.getPublisher(id)) {
        articleMapper.deleteArticle(id);
    }
        return "redirect:/article";
    }


}



