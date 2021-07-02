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
import java.text.DecimalFormat;
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
            if(te.getState()==0||te.getState()==2) allProblems.add(te);
        }

        int sumDoUser=0;//管理员所有题目的总提交用户数
        int sumPassUser=0;//管理员所有题目的总通过用户数
        int sumNotPassSubmit =0;//没通过每道题的用户，每道题所提交的次数的总和
        int sumNotPassUserCount =0;//没通过题目的总用户数
        int sumPassSubmit =0;//通过每道题的用户，每道题所提交的次数的总和
        int sumPassUserCount =0;//通过题目的总用户数

        for(Problem problem:allProblems){

            /*
            计算每道题未通过的人平均提交次数
            */
            UserProblem tempSubmit = new UserProblem();
            if(tempSubmit.notPassAverageSubmit(problem.getId())==null){
                sumNotPassSubmit += 0;
                sumNotPassUserCount += 0;
            }else {
                ArrayList<Integer> tempNotPass = tempSubmit.notPassAverageSubmit(problem.getId());
                sumNotPassSubmit += tempNotPass.get(0);
                sumNotPassUserCount += tempNotPass.get(1);
            }
            /*
            每道题通过的人平均提交次数，只截至到第一次AC时的提交次数
             */
            if(tempSubmit.passAverageSubmit(problem.getId())==null){
                sumPassSubmit += 0;
                sumPassUserCount += 0;
            }else {
                ArrayList<Integer> tempPass = tempSubmit.passAverageSubmit(problem.getId());
                sumPassSubmit += tempPass.get(0);
                sumPassUserCount += tempPass.get(1);
            }

            int doCount=userProblemMapper.doCount(problem.getId());
            int passCount=userProblemMapper.passCount(problem.getId());
            sumDoUser+=doCount;
            sumPassUser+=passCount;

        }



        /*
        以下都是总体情况的统计
         */
        model.addAttribute("sumPassUser",sumPassUser);
        model.addAttribute("sumDoUser",sumDoUser);
        double SumPassRate;
        SumPassRate=(double)sumPassUser/(double)sumDoUser;
        SumPassRate*=100;
        DecimalFormat dec = new DecimalFormat("0.00");
        String sumPassRate= dec.format(SumPassRate);
        sumPassRate+="%";
        model.addAttribute("sumPassRate",sumPassRate);

        if(sumNotPassUserCount==0)model.addAttribute("sumNotPassAverageSubmit",-1);
        else model.addAttribute("sumNotPassAverageSubmit",sumNotPassSubmit/sumNotPassUserCount);

        if(sumPassUserCount==0)model.addAttribute("sumPassAverageSubmit",-1);
        else model.addAttribute("sumPassAverageSubmit",sumPassSubmit/sumPassUserCount);


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


    @GetMapping("/personal")
    public void personal(Model model, HttpServletRequest request,HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();

        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return;
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
            if(te.getState()==0||te.getState()==2) allProblems.add(te);
        }

        int sumDoUser=0;//管理员所有题目的总提交用户数
        int sumPassUser=0;//管理员所有题目的总通过用户数
        int sumNotPassSubmit =0;//没通过每道题的用户，每道题所提交的次数的总和
        int sumNotPassUserCount =0;//没通过题目的总用户数
        int sumPassSubmit =0;//通过每道题的用户，每道题所提交的次数的总和
        int sumPassUserCount =0;//通过题目的总用户数
        for(Problem problem:allProblems){
            AdminInfo temp=new AdminInfo();
            temp.setId(problem.getId());
            temp.setTitle(problem.getTitle());
            temp.setDifficulty(problem.getDifficulty());
            temp.setPublishTime(problem.getPublishTime());
            temp.setState(problem.getState());

            /*
            计算每道题未通过的人平均提交次数
            */
            UserProblem tempSubmit = new UserProblem();
            if(tempSubmit.notPassAverageSubmit(problem.getId())==null){
                sumNotPassSubmit += 0;
                sumNotPassUserCount += 0;
                temp.setNotPassAverageSubmit(0);//每道题没通过平均提交次数
            }else {
                ArrayList<Integer> tempNotPass = tempSubmit.notPassAverageSubmit(problem.getId());
                sumNotPassSubmit += tempNotPass.get(0);
                sumNotPassUserCount += tempNotPass.get(1);
                temp.setNotPassAverageSubmit(tempNotPass.get(2));//每道题没通过平均提交次数
            }
            /*
            每道题通过的人平均提交次数，只截至到第一次AC时的提交次数
             */
            if(tempSubmit.passAverageSubmit(problem.getId())==null){
                sumPassSubmit += 0;
                sumPassUserCount += 0;
                temp.setPassAverageSubmit(0);
            }else {
                ArrayList<Integer> tempPass = tempSubmit.passAverageSubmit(problem.getId());
                sumPassSubmit += tempPass.get(0);
                sumPassUserCount += tempPass.get(1);
                temp.setPassAverageSubmit(tempPass.get(2));
            }

            int doCount=userProblemMapper.doCount(problem.getId());
            int passCount=userProblemMapper.passCount(problem.getId());
            sumDoUser+=doCount;
            sumPassUser+=passCount;
            double rate;
            if(doCount!=0){
                rate=((double)passCount)/doCount;
                rate=rate*100;
            }
            else rate=-1;
            DecimalFormat dec = new DecimalFormat("0.00");
            String s= dec.format(rate);
            temp.setPassRate(s);
            temp.setUserCount(doCount);
            temp.setPassCount(passCount);


            adminInfoList.add(temp);
        }
        //model.addAttribute("adminInfoList", adminInfoList);
        String jsonRank= JSON.toJSON(adminInfoList).toString();
        //model.addAttribute("problemRanks", problemRanks);

        System.out.println(adminInfoList);

        jsonRank="{\"code\":0,\"msg\":\"\",\"count\":"+adminInfoList.size()+",\"data\":"+jsonRank+"}";
        System.out.println(jsonRank);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonRank);

        /*
        以下都是总体情况的统计
         */
//        model.addAttribute("sumPassUser",sumPassUser);
//        model.addAttribute("sumDoUser",sumDoUser);
//        double SumPassRate;
//        SumPassRate=(double)sumPassUser/(double)sumDoUser;
//        DecimalFormat dec = new DecimalFormat("0.00");
//        String sumPassRate= dec.format(SumPassRate);
//        model.addAttribute("sumPassRate",sumPassRate);
//
//        if(sumNotPassUserCount==0)model.addAttribute("sumNotPassAverageSubmit",-1);
//        else model.addAttribute("sumNotPassAverageSubmit",sumNotPassSubmit/sumNotPassUserCount);
//
//        if(sumPassUserCount==0)model.addAttribute("sumPassAverageSubmit",-1);
//        else model.addAttribute("sumPassAverageSubmit",sumPassSubmit/sumPassUserCount);

//        response.getWriter().write(sumPassUser);
//        response.getWriter().write(sumDoUser);
//        double SumPassRate;
//        SumPassRate=(double)sumPassUser/(double)sumDoUser;
//        DecimalFormat dec = new DecimalFormat("0.00");
//        String sumPassRate= dec.format(SumPassRate);
//        //model.addAttribute("sumPassRate",sumPassRate);
//        response.getWriter().write(sumPassRate);
//
//        if(sumNotPassUserCount==0) response.getWriter().write("-1");
//        else  response.getWriter().write(sumNotPassSubmit/sumNotPassUserCount);
//
//        if(sumPassUserCount==0) response.getWriter().write("-1");
//        else response.getWriter().write(sumPassSubmit/sumPassUserCount);



    }




}
