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

    class MyThread extends Thread {
        User user;
        String problemId;
        int pId, finalExId;
        StringBuilder sb;

        public MyThread(User user, String problemId, int pId, int finalExId, StringBuilder sb) {
            this.user = user;
            this.problemId = problemId;
            this.pId = pId;
            this.finalExId = finalExId;
            this.sb = sb;
        }

        Process p = null;
        @Override
        public void run() {
            try {
                p = Runtime.getRuntime().exec(" .\\\\" + user.getId() + "_" + problemId + ".exe");
            } catch (IOException e) {
                File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                fileDc.delete();
                fileDe.delete();
                e.printStackTrace();
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

            try {
                bw.write(exampleMapper.getInputByIdAndExampleId(pId, finalExId).getContent());
            } catch (IOException e) {
                File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                fileDc.delete();
                fileDe.delete();
                e.printStackTrace();
            }

            try {
                bw.close();
            } catch (IOException e) {
                File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                fileDc.delete();
                fileDe.delete();
                e.printStackTrace();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = null;

            //若是死循环，回停在下一步读输出流。不管有没有输出都在后面停下
            while (true) {
                try {
                    if (!((line = br.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append(line + " ");
            }
        }
    }

    @PostMapping("/code_submit")
    public void login(
            @RequestParam(value = "userText", required = false) final String userText,
            @RequestParam(value = "problemId", required = false) final String problemId,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model
    ) throws InterruptedException, IOException {

            Cookie[] cookies = request.getCookies();
            model = cookieCheck.check(cookies, model);
            User user = (User) model.getAttribute("User");
            int pId = Integer.parseInt(problemId);
            File file = new File(user.getId() + "_" + problemId + ".cpp");
        //System.out.println(userText);
        try {
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

                Process p1 = Runtime.getRuntime().exec(commandStr);
                p1.waitFor();

                File fileE = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                if (fileE.exists()) {//判断是否编译成功

                    long executeTime = 0;

                    for (int exId = 0; exId < 3; exId++) {

                        StringBuilder sb = new StringBuilder();
                        int finalExId = exId;

                        MyThread thread = new MyThread(user, problemId, pId, finalExId, sb);
                        thread.start();

                        long startTime=System.currentTimeMillis();
                        for (;thread.isAlive() && System.currentTimeMillis() - startTime < 10000;);
//                        System.out.println(thread.isAlive());
                        executeTime+=(System.currentTimeMillis()-startTime);//?
                        if(System.currentTimeMillis() - startTime >= 10000){
                            thread.interrupt();//?
                            thread.p.destroy();
                            thread.p.waitFor();//destroy也需要wait
                            File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                            File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                            fileDc.delete();
                            fileDe.delete();
                            response.getWriter().write("Time Limit Exceeded");
                            userProblemMapper.insertUserProblem(user.getId(), pId, 4, -1,userText);
                            System.out.println("Time Limit Exceeded");
                            return;
                        }
                        if(sb.length()!=0)
                            sb.deleteCharAt(sb.length() - 1);

                        if (!sb.toString().equals(exampleMapper.getOutputByIdAndExampleId(pId, exId).getContent())) {
                            //删除可执行程序和源程序
                            File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                            File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                            fileDc.delete();
                            fileDe.delete();
                            //输出错误信息,未通过样例,传到前端
                            response.getWriter().write("Wrong Answer!");
                            userProblemMapper.insertUserProblem(user.getId(), pId, 1, -1,userText);
                            System.out.println("Wrong Answer");
                            return;
                        }
                    }

//                    int usedTime = (int) (endTime - startTime);
                    //删除可执行程序和源程序 暂时不删  debug
                    File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                    File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                    fileDc.delete();
                    fileDe.delete();
                    response.getWriter().write("Accept");
                    userProblemMapper.insertUserProblem(user.getId(), pId, 0, (int)executeTime,userText);
                    System.out.println("Accept");
                    return;
                } else if (!fileE.exists()) {
                    File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
                    File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
                    fileDc.delete();
                    fileDe.delete();
                    //如果可以，在这里输出错误信息
                    response.getWriter().write("Compile Error");
                    userProblemMapper.insertUserProblem(user.getId(), pId, 2, -1,userText);
                }
            } else {
                response.getWriter().write("Judging");
                System.out.println("正在判题" + user.getId() + "_" + problemId);
                return;
            }


        } catch(IOException e){
            System.out.println("IO catch");
            File fileDc = new File(".\\\\" + user.getId() + "_" + problemId + ".cpp");
            File fileDe = new File(".\\\\" + user.getId() + "_" + problemId + ".exe");
            fileDc.delete();
            fileDe.delete();
            response.getWriter().write("Unexpected Error");
            return;
        }
    }
}
