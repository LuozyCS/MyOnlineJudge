package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.User;
import judge.mapper.ExampleMapper;
import judge.mapper.ProblemMapper;
import judge.mapper.UserMapper;
import judge.mapper.UserProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class AcController {

    //需要exampleMapper
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private ExampleMapper exampleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CookieCheck cookieCheck;
    @Autowired
    private UserProblemMapper userProblemMapper;

    @PostMapping("/code_submit")
    public void login(
            @RequestParam(value = "userText", required = false) final String userText,
            @RequestParam(value = "problemId", required = false) final String problemId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) throws IOException, InterruptedException {
        Cookie[] cookies = request.getCookies();
        model = cookieCheck.check(cookies, model);
        User user = (User) model.getAttribute("User");
        int pId = Integer.parseInt(problemId);
        File file = new File(user.getId() + "_" + problemId + ".cpp");
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("创建成功" + user.getId() + "_" + problemId);
            } catch (IOException e) {
                e.printStackTrace();
            }


            FileWriter fileWrite = new FileWriter(file);//覆盖写入
            fileWrite.write(userText);
            fileWrite.close();



            String commandStr = "g++ -o " + user.getId() + "_" + problemId + ".exe " + user.getId() + "_" + problemId + ".cpp ";
//            保留可能输出错误信息的希望
//            + " > " + ".\\" + user.getId() + "_" + problemId + "_debug.txt 2>&1"


            BufferedReader br;
            BufferedWriter bw;
            Process p1 = Runtime.getRuntime().exec(commandStr);
            p1.waitFor();

            File fileE = new File(".\\\\"+user.getId() + "_" + problemId + ".exe");
            if (fileE.exists()) {//判断是否编译成功
                long startTime = System.currentTimeMillis();//计时
                for (int exId = 0; exId < 1; exId++) {
                    commandStr = " .\\\\" + user.getId() + "_" + problemId + ".exe";
                    Process p = Runtime.getRuntime().exec(commandStr);
                    bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//                System.out.println(pId);
//                System.out.println(exId);
//                System.out.println(exampleMapper.getInputByIdAndExampleId(pId, exId));
                    bw.write(exampleMapper.getInputByIdAndExampleId(pId, exId).getContent());
                    bw.close();
                    br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + " ");
                    }
                    sb.deleteCharAt(sb.length() - 1);
//                    System.out.println(sb.toString());
//                    System.out.println(sb.toString().length());
//                    System.out.println(exampleMapper.getOutputByIdAndExampleId(pId, exId).getContent().length());
//                    System.out.println(exampleMapper.getOutputByIdAndExampleId(pId, exId).getContent());
                    if (!sb.toString().equals(exampleMapper.getOutputByIdAndExampleId(pId, exId).getContent())) {
                        //删除可执行程序和源程序
                        File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                        File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                        fileDc.delete();
                        fileDe.delete();
                        //输出错误信息,未通过样例,传到前端
                        response.getWriter().write("Wrong Answer!");
                        userProblemMapper.insertUserProblem(user.getId(),pId,1,-1);
                        System.out.println("Wrong Answer");
                        return;
                    }
                }
                long endTime = System.currentTimeMillis();
                int usedTime = (int)(endTime-startTime);
                //删除可执行程序和源程序 暂时不删  debug
                File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                fileDc.delete();
                fileDe.delete();
                response.getWriter().write("Accept");
                userProblemMapper.insertUserProblem(user.getId(),pId,0,usedTime);
                System.out.println("Accept");
                return;
            }
            else if (!fileE.exists()) {
                //如果可以，在这里输出错误信息
                response.getWriter().write("Compile Error");
                userProblemMapper.insertUserProblem(user.getId(),pId,2,-1);
                File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                fileDc.delete();
            }
        } else {
            response.getWriter().write("Judging");
            System.out.println("正在判题" + user.getId() + "_" + problemId);
            return;
        }


    }
}
