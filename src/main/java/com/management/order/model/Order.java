package com.management.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="orders")
@Getter @Setter
@Builder
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    @Column
    private Date orderDate;

    @Column
    private Date deliveryDate;

    @Column
    private String orderNumber;

    @Column
    private String title;

    @Column
    private boolean completed;

    @Column
    private boolean deleted;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    public Order() {

    }
}
