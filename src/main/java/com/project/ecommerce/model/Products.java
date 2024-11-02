package com.project.ecommerce.model;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private int price;
    @Column(name = "discount")
    private double discount;
    @Column(name = "thumbnail")
    private String thumbnail;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "create_at")
    private Date createAt = new Date();
    @Column(name = "update_at")
    private Date updateAt;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categorys categorys;
}
