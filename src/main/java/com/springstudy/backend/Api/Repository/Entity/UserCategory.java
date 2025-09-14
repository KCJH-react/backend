package com.springstudy.backend.Api.Repository.Entity;

import com.springstudy.backend.Common.Type.Challenge;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_categories")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Challenge challenge;
}
