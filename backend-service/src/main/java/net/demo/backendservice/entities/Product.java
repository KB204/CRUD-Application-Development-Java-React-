package net.demo.backendservice.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false,unique = true)
    private String name;
    private String description;
    @Column(nullable = false)
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToMany(mappedBy = "products")
    private Set<Invoice> invoices = new HashSet<>();
}
