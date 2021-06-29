package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Comment;
import judge.dataTransferObject.HistoryList;
import judge.dataTransferObject.User;
import judge.dataTransferObject.UserProblem;
import judge.dataTransferObject.HistoryList;
import judge.dataTransferObject.User;
import judge.dataTransferObject.UserProblem;
import judge.dataTransferObject.Example;
import judge.mapper.ExampleMapper;
import judge.mapper.ProblemMapper;
import judge.mapper.CommentMapper;
import judge.mapper.UserMapper;
import judge.mapper.UserProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ProblemController
{

    @Autowired private UserMapper userMapper;
    @Autowired private ProblemMapper problemMapper;
    @Autowired private CommentMapper commentMapper;
    @Autowired private CookieCheck cookieCheck;
    @Autowired private UserProblemMapper userProblemMapper;
    @Autowired private ExampleMapper exampleMapper;
    @GetMapping("/problem/id={id}")
    public String problem(
            @PathVariable("id") int id

            , Model model
            , HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        model=cookieCheck.check(cookies,model);
        if (cookieCheck.Admincheck(cookies) == false) {//不是管理员就隐藏右上角按钮
            model.addAttribute("failed", "you are not administrator");
        }
        User user= (User) model.getAttribute("User");
        if(problemMapper.getState(id)==1&&user.getId()!=problemMapper.getPublisher(id)){
            model.addAttribute("NotPublisher","you are not the publisher of this problem");
            return "redirect:/list";
        }
//        System.out.println(((User)model.getAttribute("User")).getId());
//        System.out.println("problem controller " + id);
        model.addAttribute("ProblemContent", problemMapper.getContent(id));
        model.addAttribute("ProblemTitle", problemMapper.getTitle(id));
        model.addAttribute("ProblemState",problemMapper.getState(id));
        model.addAttribute("ProblemId",id);
        List<Comment> directComments = commentMapper.getDirectComments(id);
        model.addAttribute("DirectComments", directComments);
        for (Comment comment : directComments)
            comment.setPublisher(userMapper.getUserById(comment.getPublisher().getId()));

        int flag=problemMapper.getDifficulty(id);
        String temp;
        if(flag==0){
            temp="简单";
        }else if(flag==1){
            temp="中等";
        }else{
            temp="困难";
        }
        model.addAttribute("ProblemDifficulty",temp);
        //接下来将要获得第一个样例输入以及输出
        Example example1=exampleMapper.getFirstInputById(id);
        model.addAttribute("inputExample",example1);
        Example example2=exampleMapper.getFirstOutputById(id);
        model.addAttribute("outputExample", example2);

        String user_id;
        user_id=request.getParameter("user_id");

        if(user_id!=null){
            int uid=Integer.parseInt(user_id);
            Timestamp when=new Timestamp(System.currentTimeMillis());
            when=Timestamp.valueOf(request.getParameter("when"));
            System.out.println(when);
                    //request.getParameter("when");
            String content=userProblemMapper.getSubmitContent(uid, id, when);
            model.addAttribute("content", content);
        }


//        System.err.println(model.getAttribute("Comments"));
        return "site/problem-details";
    }

    @RequestMapping("/list")
    public String pagelist(
            HttpServletRequest request
            ,Model model
    ){
        Cookie[] cookies = request.getCookies();
        model = cookieCheck.check(cookies,model);
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就隐藏按钮
            model.addAttribute("failed","you are not administrator");
        }

        //在此添加排行榜与提交历史内容
        User user=(User)model.getAttribute("User");
        List<UserProblem> list = userProblemMapper.getAllByUserId(user.getId());
        ArrayList<HistoryList> historyLists = new ArrayList<>(list.size());
        for(UserProblem each: list){
            HistoryList temp =new HistoryList();
            temp.setUserProblem(each);
            temp.setProblemTitle(problemMapper.getTitle(each.getProblem_id()));
            temp.setDifficulty(problemMapper.getDifficulty(each.getProblem_id()));
            historyLists.add(temp);
        }
        Collections.reverse(historyLists);//倒序排历史表
        model.addAttribute("HistoryList",historyLists);


        return "admin/page_list";
    }
}
