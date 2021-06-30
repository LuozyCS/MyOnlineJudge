package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Problem;
import judge.dataTransferObject.User;
import judge.mapper.ExampleMapper;
import judge.mapper.ProblemMapper;
import judge.mapper.UserMapper;
import judge.mapper.UserProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.peer.ListPeer;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProblemlistController {
    @Autowired private UserMapper userMapper;
    @Autowired private ProblemMapper problemMapper;
    @Autowired private CookieCheck cookieCheck;
    @Autowired private ExampleMapper exampleMapper;


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

        //管理员可以看到所有题目，除了草稿
        List<Problem> userProblemList = problemMapper.getAllExceptContent();

        for(int i=0;i<userProblemList.size();i++){
            if(userProblemList.get(i).getState()==1){

                userProblemList.remove(i);
            }
        }

        model.addAttribute("Problems", userProblemList);

        for(Problem a: userProblemList){
            System.out.println(a.getTitle());
        }

        return "admin/admin_problem_list";

    }



    @GetMapping("/admin_draft_list")
    public String draftlist(Model model,
                              HttpServletResponse response,
                              HttpServletRequest request
    )
    {
        //确认cookie
        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }
        model = cookieCheck.check(cookies,model);
        User user=(User) model.getAttribute("User");

        //草稿箱状态为1
        List<Problem> userDraftList = problemMapper.getAllExceptContent_ByPublisherId(user.getId());
        ArrayList<Problem> ans=new ArrayList<>();
        for(int i = 0;i<userDraftList.size();i++){
            if(userDraftList.get(i).getState()==1) ans.add(userDraftList.get(i));
        }


        model.addAttribute("Problems", ans);

        return "admin/admin_draft_list";

    }




    //用于删除题目的controller
    @GetMapping("/delete_request/id={id}") public String deleteProblem(
            @PathVariable("id") int id,
            Model model,
            HttpServletResponse response,
            HttpServletRequest request)
    {
        //这个controller是草稿题目的删除

        System.out.println("delete controller");
        Cookie[] cookies = request.getCookies();
        model=cookieCheck.check(cookies,model);
        User user=(User) model.getAttribute("User");
        if((cookieCheck.Admincheck(cookies)==false)&&(user.getId()!=problemMapper.getPublisher(id))){//不是管理员或草稿本人就撤回回list
            return "redirect:/list";
        }
        exampleMapper.deleteExample(id);
        problemMapper.deleteProblem(id);


        return "redirect:/admin_draft_list";
    }

    @GetMapping("/abandon_request/id={id}") public String abandonProblem(
            @PathVariable("id") int id,
            Model model,
            HttpServletResponse response,
            HttpServletRequest request)
    {
        //这个controller改为废弃

        System.out.println("abandon controller");
        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }

        problemMapper.abandonProblem(id);

        return "redirect:/admin_problem_list";
    }


}



