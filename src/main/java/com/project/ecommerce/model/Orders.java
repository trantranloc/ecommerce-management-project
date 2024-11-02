package com.project.ecommerce.model;

import com.project.ecommerce.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "note")
    private String note;
    @Column(name = "status")
    private Status status;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "total_money")
    private double totalMoney;
    @Column(name = "quantity")
    private int quantity;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;



}
