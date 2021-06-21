package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Comment;
import judge.dataTransferObject.User;
import judge.mapper.ArticleMapper;
import judge.mapper.CommentMapper;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Controller public class CommentController
{
    @Autowired private ArticleMapper articleMapper;
    @Autowired private CommentMapper commentMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private CookieCheck cookieCheck;

//    @ResponseBody
//    @RequestMapping("/comment/indirect comments")
    @RequestMapping("/article/indirect comments of comment {comment_id} in article {article_id}")
    public String getIndirectMappers(
            @PathVariable("comment_id") final int commentId
            , @PathVariable("article_id") final int articleId
            , RedirectAttributesModelMap model
//            Model model
            , HttpServletRequest request) throws IOException
    {
//        final int commentId = Integer.parseInt(request.getParameter("id"));
//        final int articleId = Integer.parseInt(request.getParameter("articleid"));
//        HashMap<String, Object> data = new HashMap<String, Object>(2);
//        data.put("origin_id", commentId);
        model.addFlashAttribute("origin_id", commentId);
        List<Comment> comments = commentMapper.getComments(articleId, commentId);
//        System.out.println(comments);
//        data.put("Comments", comments);
        model.addFlashAttribute("Comments", comments);
        for (Comment comment : comments)
        {
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));
            for (Comment forParent : comments)
                if (comment.getParent().getId() == forParent.getId())
                {
                    comment.setParent(forParent);
                    break;
                }
        }
//        response.getWriter().write(new JSONObject(data).toString());
//        request.setAttribute("origin_id", commentId);
//        request.setAttribute("Comments", comments);
//        System.err.println(new JSONObject(data));
        return "redirect:/article/id="+articleId;
    }

    @RequestMapping({"comment/first","comment/second"})
    public String publishComment(Model model, HttpServletRequest request)
    {
        cookieCheck.check(request.getCookies(), model);
        final int articleId = Integer.parseInt(request.getParameter("articleid"));
        final int parentId = Integer.parseInt(request.getParameter("parentid"));
        final int originIdOfParent = parentId == -1 ? -2 : commentMapper.getOriginId(articleId, parentId);

        String content = request.getParameter("content");
        if (content == null || content.isEmpty())
        {
            model.addAttribute("comment error", "评论不能为空");
            return "site/blog-details";
        }
        else if (content.length() > 500)
        {
            model.addAttribute("comment error", "评论不能超过500字\n");
            return "site/blog-details";
        }

        commentMapper.publishComment(
                articleId
                ,((User)model.getAttribute("User")).getId()
                ,originIdOfParent == -2 ? -1 : (originIdOfParent == -1 ? parentId : originIdOfParent)
                ,parentId
                ,content);

        List<Comment> directComments = commentMapper.getDirectComments(articleId);
        System.out.println(directComments);
        model.addAttribute("DirectComments", directComments);
        for (Comment comment : directComments)
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));

        return "site/blog-details";
    }

    @RequestMapping("comment/third")
    public String deleteComment(Model model, HttpServletRequest request)
    {
//        cookieCheck.check(request.getCookies(), model);
//        if (((String)(model.getAttribute("LoginName"))).equals(request.getParameter("username")))
        final int articleId = Integer.parseInt(request.getParameter("articleid"));
        commentMapper.clearComment(articleId, Integer.parseInt(request.getParameter("id")));
        List<Comment> directComments = commentMapper.getDirectComments(articleId);
        System.out.println(directComments);
        model.addAttribute("DirectComments", directComments);
        for (Comment comment : directComments)
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));
        return "site/blog-details";
    }
}
