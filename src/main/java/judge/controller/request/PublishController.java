package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.Example;
import judge.dataTransferObject.Problem;
import judge.dataTransferObject.User;
import judge.mapper.ExampleMapper;
import judge.mapper.ProblemMapper;
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
    private ProblemMapper problemMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CookieCheck cookieCheck;
    @Autowired
    private ExampleMapper exampleMapper;

    //发布界面发布
    @PostMapping("/addProblem")
    public String addProblem(
            Problem problem
            ,String in01
            ,String in02
            ,String in11
            ,String in12
            ,String in21
            ,String in22
            ,String select
            , HttpServletRequest request
            , Model model
    ){

        Cookie[] cookies = request.getCookies();

        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }

        System.out.println(select);

        //导入题目内容
        model=cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");
        problem.setPublisher(user);
        if(select=="简单"){
            problem.setDifficulty(0);
        }else if(select=="困难"){
            problem.setDifficulty(1);
        }else{
            problem.setDifficulty(2);
        }
        String ErrorMessages= problem.getErrorMessages();
        if (ErrorMessages==null) {
            problem.setState(0);
            int[] insertBack = problemMapper.insertProblem(problem);
            if (0==insertBack[1]) {
                //插入失败
                model.addAttribute("failedInsert", true);
                model.addAttribute("oldcontent", problem.getContent());
                model.addAttribute("oldTitle",problem.getTitle());

                model.addAttribute("oldInExample0",in01);
                model.addAttribute("oldInExample1",in11);
                model.addAttribute("oldInExample2",in21);
                model.addAttribute("oldOutExample0",in02);
                model.addAttribute("oldOutExample1",in12);
                model.addAttribute("oldOutExample2",in22);
                model.addAttribute("oldDifficulty", problem.getDifficulty());
                return "admin/publish";
            }
            problem.setId(insertBack[0]);
        }
        else
            model.addAttribute("failed",ErrorMessages);

        //导入测试样例,未作为空处理的约束条件
        Example example=new Example();
        example.setAll(problem.getId(), 0,0,in01);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 1,0,in02);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 0,1,in11);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 1,1,in12);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 0,2,in21);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 1,2,in22);
        exampleMapper.insertExample(example);



        //点击发布题目后，发布一个新的题目会到这
        System.out.println("addProblem");
        return "redirect:/admin_problem_list";
    }

    //发布界面暂存
    @PostMapping("/addDraft")
    public String addDraft(
            Problem problem
            ,String in01
            ,String in02
            ,String in11
            ,String in12
            ,String in21
            ,String in22
            ,String select
            , HttpServletRequest request
            , Model model
    ){

        Cookie[] cookies = request.getCookies();

        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }

        System.out.println(select);

        //导入题目内容
        model=cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");
        problem.setPublisher(user);
        if(select=="简单"){
            problem.setDifficulty(0);
        }else if(select=="困难"){
            problem.setDifficulty(1);
        }else{
            problem.setDifficulty(2);
        }
        String ErrorMessages= problem.getErrorMessages();
        if (ErrorMessages==null) {
            problem.setState(1);
            problemMapper.insertDraft(problem);
        }
        else
            model.addAttribute("failed",ErrorMessages);

        //导入测试样例,未作为空处理的约束条件
        Example example=new Example();
        example.setAll(problem.getId(), 0,0,in01);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 1,0,in02);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 0,1,in11);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 1,1,in12);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 0,2,in21);
        exampleMapper.insertExample(example);

        example.setAll(problem.getId(), 1,2,in22);
        exampleMapper.insertExample(example);



        //点击发布题目后，发布一个新的题目会到这
        System.out.println("addDraft");
        return "redirect:/admin_draft_list";
    }



    //草稿箱界面发布
    @PostMapping("/updateDraftToProblem")
    public String updateDraftToProblem(
            Problem problem
            ,String in01
            ,String in02
            ,String in11
            ,String in12
            ,String in21
            ,String in22
            , HttpServletRequest request
            , Model model
    ){

        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }
//        System.out.println("这是example0：");
//        System.out.println(request.getParameter("example0"));
        model=cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");
//        System.out.println(model.getAttribute("User"));

        problem.setPublisher(user);

        String ErrorMessages= problem.getErrorMessages();
        if( ErrorMessages==null || (ErrorMessages.isEmpty()) ) {
            problem.setState(0);
            boolean success=problemMapper.insertDraftToProblem(problem);
            if(!success){
                //插入失败
                model.addAttribute("failedInsert", true);
                model.addAttribute("oldcontent", problem.getContent());
                model.addAttribute("oldid",problem.getId());
                model.addAttribute("oldTitle",problem.getTitle());

                model.addAttribute("oldInExample0",in01);
                model.addAttribute("oldInExample1",in11);
                model.addAttribute("oldInExample2",in21);
                model.addAttribute("oldOutExample0",in02);
                model.addAttribute("oldOutExample1",in12);
                model.addAttribute("oldOutExample2",in22);
                model.addAttribute("oldDifficulty", problem.getDifficulty());
                return "admin/publish";
            }
        }
        else
            model.addAttribute("failed",ErrorMessages);



        //导入测试样例,未作为空处理的约束条件
        Example example=new Example();
        example.setAll(problem.getId(), 0,0,in01);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 1,0,in02);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 0,1,in11);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 1,1,in12);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 0,2,in21);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 1,2,in22);
        exampleMapper.updateExample(example);

        //点击修改后，修改题目并发布，到这
        System.out.println("updateDraftToProblem");
        return "redirect:/admin_problem_list";
    }


    //草稿箱界面暂存
    @PostMapping("/updateDraft")
    public String updateDraft(
            Problem problem
            ,String in01
            ,String in02
            ,String in11
            ,String in12
            ,String in21
            ,String in22
            , HttpServletRequest request
            , Model model
    ){

        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }
//        System.out.println("这是example0：");
//        System.out.println(request.getParameter("example0"));
        model=cookieCheck.check(cookies,model);
        User user=(User)model.getAttribute("User");
//        System.out.println(model.getAttribute("User"));

        problem.setPublisher(user);

        String ErrorMessages= problem.getErrorMessages();
        if( ErrorMessages==null || (ErrorMessages.isEmpty()) ) {
            problem.setState(1);
            problemMapper.updateProblem(problem);
        }
        else
            model.addAttribute("failed",ErrorMessages);



        //导入测试样例,未作为空处理的约束条件
        Example example=new Example();
        example.setAll(problem.getId(), 0,0,in01);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 1,0,in02);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 0,1,in11);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 1,1,in12);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 0,2,in21);
        exampleMapper.updateExample(example);

        example.setAll(problem.getId(), 1,2,in22);
        exampleMapper.updateExample(example);

        //点击修改后，修改题目并发布，到这
        System.out.println("updateDraft");
        return "redirect:/admin_draft_list";
    }

    @RequestMapping("/publish/problem_id = {id}")
    public String modifyProblem(
            @PathVariable("id") final int id
            , Model model
            ,HttpServletRequest request
    )
    {
        //草稿修改按钮和发布题目按钮都在这
        Cookie[] cookies = request.getCookies();
        if(cookieCheck.Admincheck(cookies)==false){//不是管理员就回list
            return "redirect:/list";
        }

        model.addAttribute("oldcontent", problemMapper.getContent(id));
        model.addAttribute("oldid",id);
        model.addAttribute("old","old");
        model.addAttribute("oldTitle",problemMapper.getTitle(id));

        model.addAttribute("oldInExample0",exampleMapper.getInputByIdAndExampleId(id,0).getContent());
        model.addAttribute("oldInExample1",exampleMapper.getInputByIdAndExampleId(id,1).getContent());
        model.addAttribute("oldInExample2",exampleMapper.getInputByIdAndExampleId(id,2).getContent());
        model.addAttribute("oldOutExample0",exampleMapper.getOutputByIdAndExampleId(id,0).getContent());
        model.addAttribute("oldOutExample1",exampleMapper.getOutputByIdAndExampleId(id,1).getContent());
        model.addAttribute("oldOutExample2",exampleMapper.getOutputByIdAndExampleId(id,2).getContent());
        model.addAttribute("oldDifficulty", problemMapper.getDifficulty(id));
        //点击admin_problem_list后，点击修改，未发布之前的修改界面，到这
        System.out.println("/publish problem id Controller");
        return "admin/publish";
    }

    @RequestMapping("/publish")
    public String modifyProblem(
            HttpServletRequest request
            ,Model model
    )
    {
        Cookie[] cookies = request.getCookies();
        Boolean isAdmin=cookieCheck.Admincheck(cookies);
        if(isAdmin==true){
            //点击管理页面的发布题目会到这
            System.out.println("Publish Controller");
            return "admin/publish";
        }else{
            model.addAttribute("failed","you are not administrator");
            return "redirect:/list";
        }

    }


}
