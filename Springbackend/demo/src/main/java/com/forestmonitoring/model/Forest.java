package com.forestmonitoring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Forest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String forestToken;

    private String name;

    @OneToMany(mappedBy = "forestToken", cascade = CascadeType.ALL)
    private List<Alert> alerts;

    public Forest(String forestToken, String name) {
        this.forestToken = forestToken;
        this.name = name;
    }
}
