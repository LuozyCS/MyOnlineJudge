package judge.controller.request;

import judge.dataTransferObject.User;
import judge.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller @Component public class SignupController {
    static final Pattern pattern = Pattern.compile("Duplicate entry '(.*)' for key '(.*)'");

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/sign_up")
    public String signup(
            @RequestParam("username") final String username
            , @RequestParam("password") final String password
            , @RequestParam("email") final String email
            , @RequestParam("repassword") final String repassword
            , Model model
        )
    {

        try
        {
            User user = new User(0, username, email, password);

            String ErrorMessages=user.getErrorMessages();
            if (ErrorMessages==null)
                userMapper.insertUser(user);
            else
                model.addAttribute("failed",ErrorMessages);
        }
        catch (final Exception exception)
        {
            exception.printStackTrace();
            final Throwable cause = exception.getCause();
            if (cause instanceof SQLIntegrityConstraintViolationException)
            {
                final String message = ((SQLIntegrityConstraintViolationException)cause).getMessage();
                if (message == null)
                {
                    model.addAttribute("failed", "未知错误");
                    return "site/signup";
                }
                Matcher matcher = pattern.matcher(message);
                if (matcher.find())
                {
                    if ("email".equals(matcher.group(2)))
                    {
                        model.addAttribute("failed", "邮箱" + matcher.group(1) + "已被注册");
                        return "site/signup";
                    }
                    if ("username".equals(matcher.group(2)))
                    {
                        model.addAttribute("failed", "用户名\"" + matcher.group(1) + "\"已存在");
                        return "site/signup";
                    }
                } else
                {
                    model.addAttribute("failed", "未知错误");
                    return "site/signup";
                }
//                String sqlState = ((SQLIntegrityConstraintViolationException)cause).getSQLState();
//                if ("23000".equals(sqlState))
            }
        }
        return "redirect:login";
    }

//    public String signup(HttpServletRequest request){
//        String username=request.getParameter("username");
//        return "signup";
//    }
}
