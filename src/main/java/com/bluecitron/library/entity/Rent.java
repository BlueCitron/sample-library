package com.bluecitron.library.entity;

import com.bluecitron.library.entity.exception.UnexpectedActionException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Rent extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "loan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Enumerated(EnumType.STRING)
    private RentStatus status;

    public Rent(Member member, Book book) {
        this.status = RentStatus.RENTED;
        this.member = member;
        this.book = book;
    }

    public void _return(Member member) {
        if (this.member == member) {
            this.status = RentStatus.RETURNNED;
            this.book.setStatus(BookStatus.KEEP);
        } else {
            throw new UnexpectedActionException("대여한 본인만 반납할 수 있습니다.");
        }
    }
}
