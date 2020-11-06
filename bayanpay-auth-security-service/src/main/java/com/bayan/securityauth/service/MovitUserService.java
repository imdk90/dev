package com.bayan.securityauth.service;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bayan.securityauth.model.User;

@Repository
public interface MovitUserService {

	void save(User user);

	Optional<User> findByName(String name);
}