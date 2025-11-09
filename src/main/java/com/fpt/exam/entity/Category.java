package com.fpt.exam.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(name = "`name`", nullable = false)
    private String name;

    @Column(name = "`description`", columnDefinition = "TEXT")
    private String description;
}
