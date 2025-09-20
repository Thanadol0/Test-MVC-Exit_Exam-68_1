package com.test.mvc.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity @Table(name="pledge_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PledgeHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="project_id", nullable=false)
    private Project project;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="reward_tier_id")
    private RewardTier rewardTier;

    @Column(nullable=false, precision=14, scale=2)
    private BigDecimal amount;

    @Column(nullable=false, length=16)
    private String status; // APPROVED | REJECTED

    private String reason;

    @Column(name="created_at", nullable=false)
    private OffsetDateTime createdAt;

    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="pledge_id")
    private Pledge pledge;

    @PrePersist void pre(){ if(createdAt==null) createdAt = OffsetDateTime.now(); }
}
