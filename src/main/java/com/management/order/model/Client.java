package com.management.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "clients")
@Getter @Setter
@Builder
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "client")
    private Set<Order> orders = new HashSet<>();

    @Column(length = 50)
    private String name;

    @Column(length = 30)
    private String manager;

    @Column(length = 20)
    private String tel;

    @Column(length = 200)
    private String address;

    @Column
    private boolean bookmarked = false;

    @JsonIgnore
    @Column
    private boolean deleted = false;

    public Client() {

    }
}
