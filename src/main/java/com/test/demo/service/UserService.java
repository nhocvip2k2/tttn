package com.test.demo.service;

import com.test.demo.dto.UserCreationRequest;
import com.test.demo.dto.UserUpdateRequest;
import com.test.demo.entity.User;
import com.test.demo.enums.Role;
import com.test.demo.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
@Service
public class UserService {
	@Autowired
	private UserRespository userRespository;
	
	public User createRequest(UserCreationRequest request) {
			User user = new User();
			
			if (userRespository.existsByUsername(request.getUsername()))
				throw new RuntimeException("user da ton tai");
			user.setUsername(request.getUsername());
			HashSet<String> roles= new HashSet<>();
			roles.add(Role.USER.name());
			user.setRoles(roles);
			PasswordEncoder passwordEncoder= new BCryptPasswordEncoder(10);
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			return  userRespository.save(user);
	}
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getUsers(){
		return userRespository.findAll();
	}
	
	@PostAuthorize("returnObject.username == authentication.name ")
	public User getUser(String Id) {
		return userRespository.findById(Id).orElseThrow(()-> new RuntimeException("user not found"));
	}
	
	public User getMyinfo() {
		var context=  SecurityContextHolder.getContext();
		String name= context.getAuthentication().getName();
		return userRespository.findByUsername(name).orElseThrow(()-> new RuntimeException("user not found"));
	}
	
	public User updateUser(String userId,UserUpdateRequest request) {
		User user = getUser(userId);
		user.setUsername(request.getUsername());
		PasswordEncoder passwordEncoder= new BCryptPasswordEncoder(10);
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		return  userRespository.save(user);
	}
	
	public void deleteUser(String Id) {
		userRespository.deleteById(Id);
	}
}

