package com.bluecitron.library.entity;

import com.bluecitron.library.entity.exception.UnexpectedActionException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Book extends BaseTimeEntity{

    @Id
    @GeneratedValue
    @Column(name = "book_id")
    private Long id;

    @OneToOne(mappedBy = "book")
    private Rent rent;

    @OneToMany(mappedBy = "book")
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(value = EnumType.STRING)
    private BookStatus status;

    private String title;
    private String author;
    private String isbn;

    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.status = BookStatus.AVAILABLE;
    }

    public Rent loan(Member member) {
        /** 1. 이용 가능한 상태의 책만 예약 가능 **/
        if (this.status != BookStatus.AVAILABLE)
            throw new UnexpectedActionException("이 책은 현재 이용 가능한 상태가 아닙니다.");

        /** 2. 대여가능 권수를 초과한 멤버는 대여 불가능 **/
        long count = member.getRents().stream()
                .map(loan -> loan.getStatus() == RentStatus.RENTED)
                .count();
        if (count >= member.getGrade().getRentalLimit())
            throw new UnexpectedActionException("대여 가능 한도를 초과하였습니다.");

        this.status = BookStatus.LOAN;
        Rent rent = new Rent(member, this);
        this.rent = rent;
        member.getRents().add(rent);
        return rent;
    }

    public Reservation reserve(Member member) {
        /** 1. 대여 중인 책만 대여 가능 **/
        if (this.status != BookStatus.LOAN)
            throw new UnexpectedActionException("이 책은 현재 예약 가능한 상태가 아닙니다.");

        /** 2. 본인이 예약하기 불가능 **/
        if (this.rent.getMember() == member)
            throw new UnexpectedActionException("대여 중인 본인은 예약할 수 없습니다.");

        /** 3. 예약이 3권 이상인 멤버는 예약 불가능 **/
        if (member.getReservations().size() > 3)
            throw new UnexpectedActionException("3권 이상의 책을 예약할 수 없습니다.");

        this.status = BookStatus.RESERVED;
        Reservation reservation = new Reservation(member, this);
        this.reservations.add(reservation);
        member.getReservations().add(reservation);
        return reservation;
    }

    /** Setter **/
    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
