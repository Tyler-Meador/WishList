package com.meador.wishlist.controller;

import com.meador.wishlist.model.Item;
import com.meador.wishlist.model.Users;
import com.meador.wishlist.model.WishList;
import com.meador.wishlist.service.UsersService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    public List<WishList> operateWishList(@RequestBody WishList wishList, @PathVariable String userName, @PathVariable String operation){

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

    @PostMapping(value = "/{userName}/{operation}/{wishListId}/item")
    public List<Item> operateItemList(@RequestBody Item item, @PathVariable String userName, @PathVariable String operation, @PathVariable int wishListId){
        Users temp = usersService.getUserByUserName(userName);

        for (WishList list : temp.getWishLists()) {
            log.info("Searching for wishListId " + wishListId + " current list -> " + list);
            if(list.getId() == wishListId){
                switch(operation){
                    case "add" -> {
                        log.info("Searching for item in wishlist -> " + list);
                        if(list.getItemList().stream().noneMatch(item1 -> item1.getItemName().equals(item.getItemName()) || item1.getId() == item.getId())){
                            log.info("<------- New Item Detected ------->");
                            log.info("Adding new item " + item + " to wishlist -> " + list);
                            list.getItemList().add(item);
                            log.info("Saving changes");
                            usersService.addUser(temp);
                            return list.getItemList();
                        }
                    }
                    case "delete" -> {
                        log.info("Searching for item in wishlist -> " + list);
                        if(list.getItemList().stream().anyMatch(item1 -> item1.getId() == item.getId())){
                            log.info("Item found, removing item from list " + list);
                            list.getItemList().removeIf(item1 -> item1.getId() == item.getId());
                            log.info("Saving changes");
                            usersService.addUser(temp);
                            return list.getItemList();
                        }
                    }
                }
            }else{
                log.error("Wishlist " + wishListId + " not found");
            }
        }
        return Collections.emptyList();
    }

}
