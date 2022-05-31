package hello.login.domain.item;

import hello.login.domain.member.Member;
import hello.login.domain.pushMysql;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;
import java.util.Date;

import static hello.login.domain.member.MemberRepository.store;

@Repository
@Slf4j
@EnableScheduling
public class CalendarRepository {
    public static List<Map<String, Calendar>> dataStore = new ArrayList<>();

    public CalendarRepository() throws SQLException {
        clearStore();
        // DB불러옴
        Connection conn;
        PreparedStatement pstmt;
        Properties jdbcProperties = new Properties();
        jdbcProperties.setProperty("user", "test");
        jdbcProperties.setProperty("password", "Alstn3599@@");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/serverdb", jdbcProperties);
        pstmt = conn.prepareStatement("SELECT * FROM  calendar");
        ResultSet rs = pstmt.executeQuery();
        //DB불러오기
        LoadingDB(rs);
    }

    private void LoadingDB(ResultSet rs) throws SQLException {
        while (rs.next()) {
            Map<String, Calendar> map = new HashMap<>();

            Calendar calendar = new Calendar();
            calendar.setLoginId(rs.getString("loginId"));
            calendar.setDate(rs.getDate("date"));
            calendar.setWater(rs.getInt("water"));
            calendar.setOx(rs.getInt("ox"));

            map.put(calendar.getLoginId(), calendar);
            dataStore.add(map);
        }


    }

    public static HashMap returnCalendar(String loginId) {
        HashMap<Date, Integer> retrunCalendar = new HashMap<>();
        for (Map<String, Calendar> stringCalendarMap : dataStore) {
            if (stringCalendarMap.get(loginId) != null) {
                System.out.println("stringCalendarMap = " + stringCalendarMap.get(loginId));
                Calendar tmp =  stringCalendarMap.get(loginId);
                retrunCalendar.put(tmp.getDate(), tmp.getWater());
            }
        }
        System.out.println("retrunCalendar = " + retrunCalendar);
        return retrunCalendar;
    }


    @Scheduled(cron="* 59 23 * * ?") // 초 분 시 일 월 요일
//    @Scheduled(cron="30 * * * * ?") // 초 분 시 일 월 요일
    public void insertCalendar() throws SQLException {
        for (String loginId : store.keySet()) {
            Member member = store.get(loginId);

            pushMysql.calendarInsertMysql(member, "INSERT INTO calendar(loginId, date, water, ox)" + "VALUE (?,?,?,?)");

            pushMysql.updateMysql("UPDATE info SET water = " + 0 + " WHERE loginid = '" + member.getLoginId() + "'");
            pushMysql.updateMysql("UPDATE info SET DailyToken = " + 0 + " WHERE loginid = '" + member.getLoginId() + "'");
            pushMysql.updateMysql("UPDATE info SET TotalDailyToken = " + (member.getTotalDailyToken() + member.getDailyToken())  + " WHERE loginid = '" + member.getLoginId() + "'");

            member.setTotalDailyToken(member.getTotalDailyToken() + member.getDailyToken());
            member.setDailyToken(0);
            member.setWater("0");

            System.out.println("저장");
        }

        new CalendarRepository();
    }

    private void clearStore() {
        dataStore.clear();
    }
}
