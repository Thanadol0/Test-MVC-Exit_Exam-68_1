package com.test.mvc.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pledge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_pledge_project"))
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_tier_id",
            foreignKey = @ForeignKey(name = "fk_pledge_reward"))
    private RewardTier rewardTier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_pledge_user"))
    private User user;

    @Column(name = "supporter_name", length = 255)
    private String supporterName;

    @Column(name = "pledged_at", nullable = false)
    private OffsetDateTime pledgedAt;

    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @PrePersist
    void prePersist() {
        if (pledgedAt == null) pledgedAt = OffsetDateTime.now();
    }

}
