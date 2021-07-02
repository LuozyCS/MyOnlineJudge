package judge.controller.request;


import com.alibaba.fastjson.JSON;
import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.*;
import judge.mapper.ExampleMapper;
import judge.mapper.ProblemMapper;
import judge.mapper.UserMapper;
import judge.mapper.UserProblemMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@Component
public class StatisticsController {
    @Autowired
    private UserMapper userMapper;
    @Autowired private ProblemMapper problemMapper;
    @Autowired private CookieCheck cookieCheck;
    @Autowired private ExampleMapper exampleMapper;
    @Autowired private UserProblemMapper userProblemMapper;

    @GetMapping("/admin_personal_list")
    public String adminPersonalList(Model model, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }

        //导入题目内容
        model=cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");

        //adminInfoList表示结果集
        List<AdminInfo>adminInfoList=new ArrayList<AdminInfo>();
        //allproblems表示所有的问题
        List<Problem> tem=problemMapper.getAllExceptContent_ByPublisherId(user.getId());
        List<Problem> allProblems=new ArrayList<Problem>();
        for(Problem te:tem){
            if(te.getState()==0) allProblems.add(te);
        }


        for(Problem problem:allProblems){
            AdminInfo temp=new AdminInfo();
            temp.setId(problem.getId());
            temp.setTitle(problem.getTitle());
            temp.setDifficulty(problem.getDifficulty());
            int doCount=userProblemMapper.doCount(problem.getId());
            int passCount=userProblemMapper.passCount(problem.getId());
            double rate;
            if(passCount!=0)rate=((double)doCount)/passCount;
            else rate=0.0;
            temp.setUserCount(doCount);
            temp.setPassCount(passCount);
            temp.setPassRate(rate);
            adminInfoList.add(temp);
        }
        model.addAttribute("adminInfoList", adminInfoList);
        //System.out.println("111111111111111111111111111111");
        System.out.println(adminInfoList);
        return "admin/admin_personal_publish_list";

    }


    @GetMapping("/admin_all_list")
    public String adminAllList(Model model, HttpServletRequest request){
        System.out.println("我是子易男粉");
        return "admin/admin_all_publish_list";
    }

    @GetMapping("/share")
    public void ajaxShare(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        System.out.println("share");
        Cookie[] cookies = request.getCookies();

        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return;
        }

        //导入题目内容
        model=cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");

        //贡献排行，获取贡献度前十的发布者
        //List<String> publishers=new ArrayList<String>();
        List<Integer>publishersId=problemMapper.getAllPublisher();

        List<Share>ans1=new ArrayList<Share>();
        for(Integer id:publishersId){
            Share temp=new Share();
            temp.setName(userMapper.getUserById(id).getUsername());
            List<Integer> allProblem=problemMapper.getAllByOnePublisher(id);
            temp.setProblemCount(allProblem.size());
            int num=0;
            for(Integer problemId:allProblem){
                num+=userProblemMapper.doCount(problemId);
            }
            temp.setDoUsers(num);
            ans1.add(temp);
        }
        System.out.println(ans1);
        String jsonShare= JSON.toJSON(ans1).toString();

        jsonShare="{\"code\":0,\"msg\":\"\",\"count\":"+ans1.size()+",\"data\":"+jsonShare+"}";
        System.out.println(jsonShare);
        response.getWriter().write(jsonShare);
    }
    @GetMapping("/rank")
    public void ajaxRank(
            Model model,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        System.out.println("rank");
        Cookie[] cookies = request.getCookies();

        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return;
        }

        //导入题目内容
        model=cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");


        //题目排行
        List<ProblemRank> problemRanks=new ArrayList<ProblemRank>();
        List<Problem> ps=problemMapper.getAllExceptContent();
        List<Problem> problems=new ArrayList<Problem>();

        for(Problem p:ps){
            if(p.getState()==0) {
                problems.add(p);
                // System.out.println(p);
            }
        }
        for(Problem problem:problems){
            ProblemRank temp=new ProblemRank();
            temp.setProblemId(problem.getId());
            temp.setTitle(problem.getTitle());
//            userMapper.getUserById(problem.getPublisher().getId()).getUsername()
            temp.setPublisher(userMapper.getUserById(problem.getPublisher().getId()).getUsername());

            temp.setCount(userProblemMapper.doCount(problem.getId()));

            problemRanks.add(temp);
        }

        String jsonRank= JSON.toJSON(problemRanks).toString();
        //model.addAttribute("problemRanks", problemRanks);

        System.out.println(problemRanks);

        jsonRank="{\"code\":0,\"msg\":\"\",\"count\":"+problemRanks.size()+",\"data\":"+jsonRank+"}";
        System.out.println(jsonRank);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonRank);
    }



}
