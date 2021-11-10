package com.meador.wishlist.service;

import com.meador.wishlist.model.Users;
import com.meador.wishlist.repository.UsersRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

   public Users addUser(Users user){
       return usersRepository.save(user);
   }

   public List<Users> getUsersList(){
       return usersRepository.findAll();
   }

   public Users getUserByUserName(String userName){
       return usersRepository.findByUserName(userName);
   }

}
