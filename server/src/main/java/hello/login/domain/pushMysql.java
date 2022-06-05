package hello.login.domain;

import hello.login.domain.item.CalendarRepository;
import hello.login.domain.member.Member;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import static hello.login.domain.member.MemberRepository.store;


public class pushMysql {

    public static void memberInsertMysql(Member member, String INSERT) throws SQLException {
        Connection conn;
        PreparedStatement pstmt = null;
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("user", "test");
        jdbcProperties.setProperty("password", "root");

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/serverdb", jdbcProperties);
        pstmt = conn.prepareStatement(INSERT);

        pstmt.setLong(1, member.getId());
        pstmt.setString(2, member.getLoginId());
        pstmt.setString(3, member.getName());
        pstmt.setString(4, member.getPassword());
        pstmt.setString(5, member.getWater());
        pstmt.setBoolean(6, member.isSex());
        pstmt.setInt(7, member.getAge());
        pstmt.setInt(8, member.getWeight());
        pstmt.setInt(9, 0);
        pstmt.setDouble(10, 0.0);
        pstmt.setDouble(11, 0.0);
        pstmt.setString(12, member.getMetamaskId());
        pstmt.executeUpdate();
    }

    public static void calendarInsertMysql(Member member, String INSERT) throws SQLException {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String time1 = format1.format(time);

        Connection conn;
        PreparedStatement pstmt = null;
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("user", "test");
        jdbcProperties.setProperty("password", "root");

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/serverdb", jdbcProperties);

        pstmt = conn.prepareStatement(INSERT);
        pstmt.setString(1, member.getLoginId());
        pstmt.setString(2, time1); //날짜
        pstmt.setString(3, member.getWater());
        pstmt.setInt(4, member.getOx());
        pstmt.executeUpdate();
    }

    public static void updateMysql(String UPDATE) throws SQLException {
        Connection conn;
        PreparedStatement pstmt = null;

        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("user", "test");
        jdbcProperties.setProperty("password", "root");

        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/serverdb", jdbcProperties);

        pstmt = conn.prepareStatement(UPDATE);
        pstmt.executeUpdate();
    }

}
