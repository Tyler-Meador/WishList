package com.meador.wishlist.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    List<WishList> wishLists;
}
