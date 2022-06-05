package hello.login.domain.member;

import hello.login.domain.pushMysql;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Slf4j
@Repository
@EnableScheduling
public class MemberRepository {

    public static Map<String, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public MemberRepository() throws SQLException {
        clearStore();
        // DB불러옴
        Connection conn;
        PreparedStatement pstmt;
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("user", "test");
        jdbcProperties.setProperty("password", "root");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/serverdb", jdbcProperties);
        pstmt = conn.prepareStatement("SELECT * FROM  info");
        ResultSet rs = pstmt.executeQuery();
        //DB불러오기
        LoadingDB(rs);
    }

    private void LoadingDB(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setLoginId(rs.getString("loginid"));
            member.setName(rs.getString("name"));
            member.setPassword(rs.getString("pw"));
            member.setWater(rs.getString("water"));
            member.setSex(rs.getBoolean("sex"));
            member.setAge(rs.getInt("age"));
            member.setWeight(rs.getInt("weight"));
            member.setDailyToken(rs.getDouble("DailyToken"));
            member.setTotalDailyToken(rs.getDouble("TotalDailyToken"));
            member.setMetamaskId(rs.getString("metamaskId"));
            store.put(member.getLoginId(), member);
            sequence = member.getId();
        }
    }


    public Member save(Member member) throws SQLException {
        member.setId(++sequence);
        log.info("save: member={}", member);

        //쿼리 푸쉬
        pushMysql.memberInsertMysql(member,"INSERT INTO info(id, loginid, name, pw, water, sex, age, weight, ox, DailyToken, TotalDailyToken, metamaskId)" + "VALUE (?,?,?,?,?,?,?,?,?,?,?,?)");
        store.put(member.getLoginId(), member);
        return member;
    }

    public Optional<Member> findByLoginId(String loginId) throws SQLException {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }



}
