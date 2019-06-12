package com.unimoni.oauth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.unimoni.oauth.model.User;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
	User findByUserId(Long userId);
    User findByLoginId(String username);
    long countByLoginIdOrEmailOrMobileNumber(String loginId, String email,String mobileNumber);
}
