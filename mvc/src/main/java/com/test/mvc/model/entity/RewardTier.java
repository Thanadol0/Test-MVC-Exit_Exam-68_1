package com.test.mvc.model.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RewardTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rewardtier_project"))
    private Project project;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "min_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal minAmount;

    @Column(name = "quota")
    private Integer quota;

}
