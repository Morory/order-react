package com.management.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="order_details")
@Getter @Setter
@Builder
@AllArgsConstructor
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Column(length = 50)
    private String name;

    @Column
    private int amount;

    @Column(length = 30)
    private String unit;

    @Column
    private int price;

    @Column
    private String taxRate;

    public OrderDetail() {

    }
}
