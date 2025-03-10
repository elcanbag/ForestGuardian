package com.forestmonitoring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String forestToken;
    private String alertType;
    private LocalDateTime timestamp;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forestToken", referencedColumnName = "forestToken", insertable = false, updatable = false)
    private Forest forest;

    public Alert(String forestToken, String alertType) {
        this.forestToken = forestToken;
        this.alertType = alertType;
        this.timestamp = LocalDateTime.now();
    }
}
