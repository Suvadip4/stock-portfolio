package com.portfolio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfolio.entities.User;

public interface UserRepo extends JpaRepository<User, Long> {

}

