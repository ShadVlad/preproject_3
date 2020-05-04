package web.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class LoginController {

    @GetMapping({"/", "/login"})
    public ModelAndView getLogin(Authentication auth,
                                 HttpServletRequest req,
                                 ModelAndView mav,
                                 HttpServletResponse res) throws IOException, ServletException {
        if (auth != null) {
            if (auth.getAuthorities().toString().equals("[admin]")) {
                res.sendRedirect("/admin");
            } else if (auth.getAuthorities().toString().equals("[user]")) {
                res.sendRedirect("/user");
            }
        }
        if (req.getParameterMap().containsKey("error")) {
            mav.setViewName("login");
            mav.addObject("status", "Не верный логин или пароль");
            return mav;
        }
        mav.setViewName("login");
        return mav;
    }

}