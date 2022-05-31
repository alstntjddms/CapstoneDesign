package hello.login.domain.item;

import hello.login.domain.member.Member;
import lombok.Data;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Calendar {
    private String loginId;
    private Date date;
    private Integer water;
    private Integer ox;

    public Calendar() {
    }

    public Calendar(String loginId, Date date, Integer water, Integer heartRate) {
        this.loginId = loginId;
        this.date = date;
        this.water = water;
        this.ox = ox;
    }

}