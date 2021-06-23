package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.User;
import judge.mapper.ExampleMapper;
import judge.mapper.ProblemMapper;
import judge.mapper.UserMapper;
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

//
//    //需要exampleMapper
//    @Autowired private ProblemMapper  problemMapper;
//    @Autowired private ExampleMapper exampleMapper;
//    @Autowired private UserMapper userMapper;
//    @Autowired private  CookieCheck cookieCheck;
//
//    @PostMapping("/code_submit") public void login(
//            @RequestParam(value = "userText", required = false) final String userText,
//            @RequestParam(value = "problemId", required = false) final String problemId,
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Model model
//    ) throws IOException, InterruptedException {
//        Cookie[] cookies=request.getCookies();
//        model= cookieCheck.check(cookies,model);
//        User user = (User) model.getAttribute("User");
//        File file = new File(user.getId()+"_"+problemId+".cpp");
//        if(!file.exists()){
//            try{
//                file.createNewFile();
//                System.out.println("创建成功"+user.getId()+"_"+problemId);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            FileWriter fileWrite=new FileWriter(file);//覆盖写入
//            fileWrite.write(userText);
//            fileWrite.close();
//
////            String commandStr = "g++ -o \\"+user.getId() + "_" + problemId + ".exe "+"\\"+user.getId()+"_"+problemId+".cpp";
//            String commandStr = "g++ .\\"+user.getId()+"_"+problemId+".cpp";
//            BufferedReader br;
//            BufferedWriter bw;
//            Process p1 = Runtime.getRuntime().exec(commandStr);
//            p1.waitFor();
//            for(int exId=0;exId<10;exId++) {
//
//                Process p = Runtime.getRuntime().exec( ".\\\\a.exe");
//
//                //Process p = Runtime.getRuntime().exec(".\\"+user.getId() + "_" + problemId + ".exe");
//                bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//                int pId = Integer.parseInt(problemId);
//                bw.write(exampleMapper.getInputByIdAndExampleId(pId, exId).getContent());
//                bw.close();
//                br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//                String line = null;
//                StringBuilder sb = new StringBuilder();
//                while ((line = br.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//                System.out.println(sb.toString());
//                if(sb.toString()!=exampleMapper.getOutputByIdAndExampleId(pId,exId).getContent()){
//                    //删除可执行程序和源程序
//                    String commandStrRm = "rm .\\"+user.getId() + "_" + problemId + ".cpp";
//                    Runtime.getRuntime().exec(commandStrRm);
//                    commandStrRm = "rm .\\"+user.getId()+"_"+problemId+".exe";
//                    Runtime.getRuntime().exec(commandStrRm);
//                    //输出错误信息,未通过样例,传到前端
//
//                    return;
//                }
//            }
//            response.getWriter().write("wangshuang123");
//            //删除可执行程序和源程序
////            String commandStrRm = "rm \\"+user.getId() + "_" + problemId + ".cpp";
////            Runtime.getRuntime().exec(commandStrRm);
////            commandStrRm = "rm \\"+user.getId()+"_"+problemId+".exe";
////            Runtime.getRuntime().exec(commandStrRm);
//            return;
//        }
//        else{
//            System.out.println("正在判题"+user.getId()+"_"+problemId);
//            return;
//        }
//    }
}
