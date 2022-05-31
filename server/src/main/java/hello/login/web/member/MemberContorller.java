package hello.login.web.member;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import static hello.login.domain.member.MemberRepository.store;
import javax.validation.Valid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberContorller {

    private final MemberRepository memberRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member){
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) throws SQLException {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/";
    }

    //아두이노 GET으로 받아옴
    @GetMapping("test")
    public String test(@RequestParam(value = "loginid") String  loginid,
                       @RequestParam(value = "water") String  water,
                       @RequestParam(value = "ox") String  ox) throws SQLException {
        System.out.println("loginid = " + loginid);
        System.out.println("water = " + water);
        System.out.println("ox = " + ox);

        //스토어 저장
        Member member = store.get(loginid);
        System.out.println("member = " + member);
        System.out.println("water = " + water);

        member.setWater(Integer.toString(Integer.parseInt(member.getWater()) + Integer.parseInt(water)));
        double target = member.getWeight()*30;
        double getToken = Double.parseDouble(member.getWater()) / target;
        member.setDailyToken(getToken);
        member.setOx(Integer.parseInt(ox));

        //쿼리 excute
        Connection conn;
        PreparedStatement pstmt = null;
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("user", "test");
        jdbcProperties.setProperty("password", "Alstn3599@@");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/serverdb", jdbcProperties);
        pstmt = conn.prepareStatement("UPDATE info SET DailyToken = " + member.getDailyToken()  + " WHERE loginid = '" + member.getLoginId() + "'");
        pstmt.executeUpdate();
        pstmt = conn.prepareStatement("UPDATE info SET water = " + member.getWater()  + " WHERE loginid = '" + member.getLoginId() + "'");
        pstmt.executeUpdate();

        return "redirect:/";
    }


}
