package com.test.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.entity.User;

@Repository
public interface UserRespository extends JpaRepository<User, String>{
	boolean existsByUsername(String username);
	Optional<User> findByUsername(String username);
}
