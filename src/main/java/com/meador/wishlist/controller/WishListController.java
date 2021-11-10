package com.meador.wishlist.controller;

import com.meador.wishlist.model.Item;
import com.meador.wishlist.model.Users;
import com.meador.wishlist.model.WishList;
import com.meador.wishlist.service.UsersService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@Log4j2
public class WishListController {

    @Autowired
    private UsersService usersService;

    @PostMapping(value = "/ping")
    @ResponseStatus(HttpStatus.OK)
    public void pingEnv(){
        log.info("Ping -> Success");
    }

    @PostMapping(value = "/register")
    public Users createUser(@RequestBody Users user){
        log.info("Adding user " + user.getUserName());
        log.info("User -> " + user.toString());
        List<WishList> tempWishList = Collections.emptyList();
        user.setWishLists(tempWishList);
        return usersService.addUser(user);
    }

    @GetMapping(value = "/profile/{userName}")
    public Users getUser(@PathVariable String userName){
        log.info("Searching for Username: " + userName);
        return usersService.getUserByUserName(userName);
    }

    @PostMapping(value = "/{userName}/create/wishlist")
    public WishList createWishList(@RequestBody WishList wishList, @PathVariable String userName){
        Users temp = usersService.getUserByUserName(userName);
        log.info("Adding new wishlist to user -> " + temp.toString());
        temp.getWishLists().add(wishList);
        usersService.addUser(temp);
        return wishList;
    }


}
