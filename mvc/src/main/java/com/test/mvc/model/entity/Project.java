package com.test.mvc.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_code", length = 8, nullable = false, unique = true)
    private String projectCode;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "goal_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal goalAmount;

    @Column(name = "deadline", nullable = false)
    private OffsetDateTime deadline;

    @Column(name = "current_balance", nullable = false, precision = 14, scale = 2)
    private BigDecimal currentBalance = BigDecimal.ZERO;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RewardTier> rewardTiers = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pledge> pledges = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "category_id")
    private Category category;

}
