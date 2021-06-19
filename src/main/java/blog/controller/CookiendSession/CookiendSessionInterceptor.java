package blog.controller.CookiendSession;

import blog.dataTransferObject.User;
import blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class CookiendSessionInterceptor implements HandlerInterceptor {
    @Autowired private UserMapper userMapper;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println(request.getRequestURL());

//        System.out.println("进入拦截器");
//        log.debug("进入拦截器");

        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie:cookies) {
//                System.out.println("cookie===for遍历"+cookie.getName());
//                log.debug("cookie===for遍历"+cookie.getName());
                if (StringUtils.equalsIgnoreCase(cookie.getName(), "isLogin")) {
//                    System.out.println("有cookie ---isLogin，并且cookie还没过期...");

                    for(Cookie cookie2:cookies) {
//                        System.out.println("cookie2===for遍历"+cookie2.getName());
                        if(StringUtils.equalsIgnoreCase(cookie2.getName(),"pwd")){
                            User user=userMapper.getUserByUsername(cookie.getValue());
                            if(cookie2.getValue().equals(user.getPwd())){
                                return true;
                            }
                            System.out.println("fault pwd cookie");
                            break;
                        }
                    }
//                    log.debug("有cookie ---isLogin，并且cookie还没过期...");
                    //遍历cookie如果找到登录状态则返回true继续执行原来请求url到controller中的方法
                }
            }
        }
//        System.out.println("没有cookie-----cookie时间可能到期，重定向到登录页面后请重新登录...");
//        log.debug("没有cookie-----cookie时间可能到期，重定向到登录页面后请重新登录...");
        response.sendRedirect("/login");
        //返回false，不执行原来controller的方法
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}

































//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Objects;
//@RestController
//public class GetCookieController {
//    @RequestMapping(value = "/getcookies", method = RequestMethod.GET)
//    public String getCookies(HttpServletResponse response) {
//        //HttpServletRequest  装请求信息的类
//        //HttpServletResponse  装相应信息的类
//        Cookie cookie = new Cookie("login", "true");
//        response.addCookie(cookie);
//        return "恭喜获得cookies信息成功";
//    }
//
//    @RequestMapping(value = "/getwithcookies",method = RequestMethod.GET)
//    public String getWithCookies(HttpServletRequest request){
//
//        Cookie[] cookies = request.getCookies();
//        if (Objects.isNull(cookies)){
//            return "cookies信息为null";
//        }
//        for (Cookie cookie:cookies){
//            if (cookie.getName().equals("login")&&cookie.getValue().equals("true")){
//                return "访问/getwithcookies接口成功";
//            }
//        }
//        return "cookies信息错误";
//    }
//}
