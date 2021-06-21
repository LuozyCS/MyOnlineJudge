package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Comment;
import judge.dataTransferObject.User;
import judge.mapper.ProblemMapper;
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
    @Autowired private ProblemMapper problemMapper;
    @Autowired private CommentMapper commentMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private CookieCheck cookieCheck;

//    @ResponseBody
//    @RequestMapping("/comment/indirect comments")
    @RequestMapping("/problem/indirect comments of comment {comment_id} in problem {problem_id}")
    public String getIndirectMappers(
            @PathVariable("comment_id") final int commentId
            , @PathVariable("problem_id") final int problemId
            , RedirectAttributesModelMap model
            , HttpServletRequest request) throws IOException
    {

        model.addFlashAttribute("origin_id", commentId);
        List<Comment> comments = commentMapper.getComments(problemId, commentId);

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

        return "redirect:/problem/id="+problemId;
    }

    @RequestMapping({"comment/first","comment/second"})
    public String publishComment(Model model, HttpServletRequest request)
    {
        cookieCheck.check(request.getCookies(), model);
        final int problemId = Integer.parseInt(request.getParameter("problemid"));
        final int parentId = Integer.parseInt(request.getParameter("parentid"));
        final int originIdOfParent = parentId == -1 ? -2 : commentMapper.getOriginId(problemId, parentId);

        String content = request.getParameter("content");
        if (content == null || content.isEmpty())
        {
            model.addAttribute("comment error", "评论不能为空");
            return "site/problem-details";
        }
        else if (content.length() > 500)
        {
            model.addAttribute("comment error", "评论不能超过500字\n");
            return "site/problem-details";
        }

        commentMapper.publishComment(
                problemId
                ,((User)model.getAttribute("User")).getId()
                ,originIdOfParent == -2 ? -1 : (originIdOfParent == -1 ? parentId : originIdOfParent)
                ,parentId
                ,content);

        List<Comment> directComments = commentMapper.getDirectComments(problemId);
        System.out.println(directComments);
        model.addAttribute("DirectComments", directComments);
        for (Comment comment : directComments)
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));

        return "site/problem-details";
    }

    @RequestMapping("comment/third")
    public String deleteComment(Model model, HttpServletRequest request)
    {
//        cookieCheck.check(request.getCookies(), model);
//        if (((String)(model.getAttribute("LoginName"))).equals(request.getParameter("username")))
        final int problemId = Integer.parseInt(request.getParameter("problemid"));
        commentMapper.clearComment(problemId, Integer.parseInt(request.getParameter("id")));
        List<Comment> directComments = commentMapper.getDirectComments(problemId);
        System.out.println(directComments);
        model.addAttribute("DirectComments", directComments);
        for (Comment comment : directComments)
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));
        return "site/problem-details";
    }
}
