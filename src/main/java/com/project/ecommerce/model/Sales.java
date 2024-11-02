package com.project.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sales")
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "start_at")
    private Date startAt ;
    @Column(name = "end_at")
    private Date endAt ;
    @Column(name = "value")
    private int value ;
    @Column(name = "code")
    private String code;
    @Column(name = "status")
    private boolean status;
}
