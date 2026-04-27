package com.OpenLeaf.modal;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "wishlists", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_book_wishlist", columnNames = {"user_id", "book_id"})
}, indexes = {
    @Index(name = "idx_user_wishlist", columnList = "user_id"),
    @Index(name = "idx_book_wishlist", columnList = "book_id")
})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime addedAt;

    @Column(length = 500)
    private String notes; // Optional notes about why the user added this book
}
