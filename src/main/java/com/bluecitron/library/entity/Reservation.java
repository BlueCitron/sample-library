package com.bluecitron.library.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Reservation extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    public Reservation(Member member, Book book) {
        this.member = member;
        this.book = book;
    }

    public void cancel() {
        this.book.setStatus(BookStatus.LOAN);
        this.member.getReservations().removeIf(reservation -> reservation == this);
    }

}
