package com.projects.BookYourShow.backend.auth.entity;


import com.projects.BookYourShow.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false,updatable = false)
    private Instant createdAt;
    @Column(nullable = false,updatable = false)
    private Instant expiredAt;

    @Column(nullable = false)
    private Boolean revoked;

    private String replacedByJti;
}
