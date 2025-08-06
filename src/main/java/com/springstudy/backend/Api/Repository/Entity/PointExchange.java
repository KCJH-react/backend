package com.springstudy.backend.Api.Repository.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "point_exchange")
public class PointExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; //포인트를 사용하는 사용자

    @Column(nullable = false)
    private int points; //교환한 포인트 수량

    @Column(nullable = false)
    private String exchangeType; //교환 유형 ("USE", "EARN")

    private LocalDateTime exchangeDate; //거래 일시

    @PrePersist
    protected void onCreate() {
        exchangeDate = LocalDateTime.now();
    }
}
