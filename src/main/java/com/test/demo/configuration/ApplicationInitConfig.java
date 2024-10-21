package com.test.demo.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.test.demo.entity.User;
import com.test.demo.enums.Role;
import com.test.demo.repository.UserRespository;

@Configuration
public class ApplicationInitConfig {
	
	PasswordEncoder passwordEncoder= new BCryptPasswordEncoder(10);
	@Bean
	ApplicationRunner applicationRunner(UserRespository userRespository) {
			
			return arg ->{
				if(userRespository.findByUsername("admin").isEmpty()) {
					var roles = new  HashSet<String>();
					User user = new User();
					user.setUsername("admin");
					roles.add(Role.ADMIN.name());
					user.setRoles(roles);	
					user.setPassword(passwordEncoder.encode("admin12345678"));
					userRespository.save(user);
				}
			};
			
		}
}
