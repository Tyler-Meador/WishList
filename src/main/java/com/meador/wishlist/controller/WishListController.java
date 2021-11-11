package com.meador.wishlist.controller;

import com.meador.wishlist.model.Users;
import com.meador.wishlist.model.WishList;
import com.meador.wishlist.service.UsersService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        log.info("User -> " + user);
        List<WishList> tempWishList = Collections.emptyList();
        user.setWishLists(tempWishList);
        return usersService.addUser(user);
    }

    @GetMapping(value = "/profile/{userName}")
    public Users getUser(@PathVariable String userName){
        log.info("Searching for Username: " + userName);
        return usersService.getUserByUserName(userName);
    }

    @PostMapping(value = "/{userName}/{operation}/wishlist")
    public List<WishList> createWishList(@RequestBody WishList wishList, @PathVariable String userName, @PathVariable String operation){

        Users temp = usersService.getUserByUserName(userName);

        switch (operation) {
            case "create" -> {
                log.info("Adding new wishlist " + wishList + " to user -> " + temp.getUserName());
                temp.getWishLists().add(wishList);
                usersService.addUser(temp);
            }
            case "delete" -> {
                log.info("Removing wishlist " + wishList + " from User -> " + temp.getUserName());
                log.info("to delete -> " + wishList.toString());
                temp.getWishLists().removeIf(list -> list.getId() == wishList.getId());
                usersService.addUser(temp);
            }
        }
        return temp.getWishLists();
    }


}
