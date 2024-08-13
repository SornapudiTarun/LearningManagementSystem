package com.example.fse2.AuthenticationMS.repository;

import com.example.fse2.AuthenticationMS.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser,Integer> {
    public AppUser findByEmail(String email);
}
