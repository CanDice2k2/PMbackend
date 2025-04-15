package com.datn.model;

import com.datn.domain.PlanType;
import com.datn.domain.SubscriptionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptiontype;

    @Enumerated(EnumType.STRING)
    private PlanType planType;

    private boolean isValid;

    @OneToOne
    private User user;

}
