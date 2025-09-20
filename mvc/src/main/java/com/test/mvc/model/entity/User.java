package com.test.mvc.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_user")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "approve_count", nullable = false)
    @Builder.Default
    private Integer approveCount = 0;

    @Column(name = "reject_count", nullable = false)
    @Builder.Default
    private Integer rejectCount = 0;


}
