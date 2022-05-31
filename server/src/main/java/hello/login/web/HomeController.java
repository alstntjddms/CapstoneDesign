package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.login.LoginForm;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import static hello.login.domain.member.MemberRepository.store;


@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }
        Object loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
    @GetMapping("dapptest")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form){
        return "/dapptest/dapptest";
    }

    @GetMapping("test1")
    public String loginForm1(@ModelAttribute("loginForm") LoginForm form){
        return "/dapptest/test1";
    }

    @GetMapping("test2")
    public String test2(@RequestParam(value = "loginid") String  loginid) {
        System.out.println("aaaaaloginid = " + loginid);
        Member member = store.get(loginid);
        member.setDailyToken(0);
        member.setTotalDailyToken(0);
        store.put(loginid, member);

        return "redirect:/";
    }


}