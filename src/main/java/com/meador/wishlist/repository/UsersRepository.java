package com.meador.wishlist.repository;

import com.meador.wishlist.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Users findByUserName(String userName);
}
