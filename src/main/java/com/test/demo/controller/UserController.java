package com.test.demo.controller;

import com.test.demo.dto.UserCreationRequest;
import com.test.demo.dto.UserUpdateRequest;
import com.test.demo.entity.User;
import com.test.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/users")
public class UserController {
	@Autowired
	private UserService userService;
	@PostMapping()
	User createUser(@RequestBody @Valid UserCreationRequest request) {

		return userService.createRequest(request);
	}
	@GetMapping()
	List<User> getUsers (){

		return userService.getUsers();
	}
	@GetMapping("/{userId}")
	User getUser(@PathVariable("userId") String userId) {

		return userService.getUser(userId);
	}
	
	@GetMapping("/myinfo")
	User getMyinfo() {
		return userService.getMyinfo();
	}
	@PutMapping("/{userId}")
	User updateUser(@PathVariable String userId,@RequestBody UserUpdateRequest request) {
		return userService.updateUser(userId, request);
	}
	@DeleteMapping("/{userId}")
	String deleteUser(@PathVariable("userId") String userId) {
		userService.deleteUser(userId);
		return "user has deleted";
	}
}
