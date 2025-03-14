package com.forestmonitoring.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private String alertType;
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forestToken", referencedColumnName = "forestToken")
    @JsonBackReference
    private Forest forest;

    public Alert(Forest forest, String alertType) {
        this.forest = forest;
        this.alertType = alertType;
        this.timestamp = LocalDateTime.now();
    }
}
