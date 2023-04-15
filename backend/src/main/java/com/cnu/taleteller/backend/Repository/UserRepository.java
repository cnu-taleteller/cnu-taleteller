package com.cnu.taleteller.backend.Repository;

import com.cnu.taleteller.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u ORDER BY u.id DESC")
    List<User> findAllDesc();
}