package com.bayan.securityauth.jpa.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bayan.securityauth.model.User;
import com.bayan.securityauth.repository.UserRepository;
import com.bayan.securityauth.service.MovitUserService;

@Service
@Transactional
public class UserService implements MovitUserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void save(User user) {
		userRepository.save(user);
	}

	@Override
	public Optional<User> findByName(String name) {
		return userRepository.findByName(name);
	}

}
