package hello.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import org.apache.catalina.valves.rewrite.Substitution;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import static hello.login.domain.member.MemberRepository.store;

@SpringBootApplication
public class ItemServiceApplication {
	public static void main(String[] args) throws SQLException {
		SpringApplication.run(ItemServiceApplication.class, args);

	}

}
