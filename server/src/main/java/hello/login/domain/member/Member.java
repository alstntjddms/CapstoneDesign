package hello.login.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Member {

    private Long id;

    @NotEmpty
    private String loginId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String password;

    private String water = "0";

    private boolean sex;

    private int weight;

    private int age;

    private int ox = 0;

    private double DailyToken = 0.0;

    private double TotalDailyToken = 0.0;

    private String metamaskId = null;


}
