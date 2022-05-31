package hello.login.web.calendar;

import hello.login.domain.item.CalendarRepository;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CalendarContoller {

    @PostMapping("/calendar")
    public String loginForm(HttpServletRequest request, Model model){
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

        Member member =(Member)loginMember;
//        CalendarRepository.returnCalendar(member.getLoginId());

        model.addAttribute("calendar", CalendarRepository.returnCalendar(member.getLoginId()));

        return "/calendar/calendar";
    }

    @GetMapping("/loginHome")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

}
