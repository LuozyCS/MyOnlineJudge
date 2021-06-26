package judge.controller.request;

import judge.controller.CookiendSession.CookieCheck;
import judge.dataTransferObject.AllProblemList;
import judge.dataTransferObject.Problem;
import judge.dataTransferObject.User;
import judge.dataTransferObject.UserProblem;
import judge.mapper.ProblemMapper;
import judge.mapper.UserProblemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Controller
public class HomepageController {
    @Autowired
    private ProblemMapper problemMapper;
    @Autowired
    private CookieCheck cookieCheck;
    @Autowired
    private UserProblemMapper userProblemMapper;

    @GetMapping({"/", "/problem"})
    public String blog(Model model,
                       HttpServletResponse response,
                       HttpServletRequest request) {
        System.out.println(cookieCheck);
        Cookie[] cookies = request.getCookies();
        model = cookieCheck.check(cookies, model);

        if (cookieCheck.Admincheck(cookies) == false) {//不是管理员就隐藏右上角按钮
            model.addAttribute("failed", "you are not administrator");
        }

        List<Problem> list = problemMapper.getAllExceptContent();
        System.out.println(list);

        //添加通过率

        //添加通过率
        ArrayList<AllProblemList> allProblemLists = new ArrayList<>(list.size());
        for (Problem eachProblem : list) {//针对每一道题进行通过率计算
            AllProblemList temp = new AllProblemList();
            ArrayList<UserProblem> forCount;
            forCount = (ArrayList<UserProblem>) userProblemMapper.getAllByProblemId(eachProblem.getId());//一道题的全部写题记录存在forCount中
            int allUser = 0, passUser = 0;
            double passRate = 0.0;
            if (!(forCount.isEmpty())) {
//            null isEmpty

                HashMap<Integer, Boolean> users = new HashMap<>(forCount.size());
                for (UserProblem userProblem : forCount) {
                    Boolean pass = users.get(userProblem.getUser_id());
                    if (pass == null) {
                        pass = userProblem.getPass() == 0;
                        users.put(userProblem.getUser_id(), pass);
                        if (pass)
                            ++passUser;
                    }
                    else if (!pass && userProblem.getPass() == 0) {
                        users.put(userProblem.getUser_id(), true);
                        ++passUser;
                    }
                }
                allUser = users.size();
                passRate = (double) passUser / (double) allUser;
            } else {
                passRate=-1;
            }

            temp.setProblem(eachProblem);
            temp.setPassRate(passRate);
            allProblemLists.add(temp);
        }

        for (AllProblemList each : allProblemLists) {
            System.out.println(each);
        }
        model.addAttribute("ProblemList", allProblemLists);
        return "site/problem";
    }
//    System.getProperty("user.dir");
}
//import java.net.http.HttpClient.Redirect;