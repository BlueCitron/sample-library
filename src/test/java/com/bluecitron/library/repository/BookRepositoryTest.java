package com.bluecitron.library.repository;

import com.bluecitron.library.entity.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void 초기화() {
        Book book1 = new Book("감정은 습관이다", "박용철", "979-11-5540-005-0");
        Book book2 = new Book("세월의돌", "전민희", "979-11-5540-982-3");

        bookRepository.save(book1);
        bookRepository.save(book2);

        Member admin = new Member("admin", "admin@admin.com");
        Member guest = new Member("guest", "guest@guest.com");
        memberRepository.save(admin);
        memberRepository.save(guest);

        Assertions.assertThat(book1.getCreatedDate()).isNotNull();
        Assertions.assertThat(book2.getCreatedDate()).isNotNull();
        Assertions.assertThat(admin.getCreatedDate()).isNotNull();
    }

    @Test
    public void 책_생성() {
        // then
        Book foundBook = bookRepository.findByTitle("감정은 습관이다");

        Assertions.assertThat(foundBook).isNotNull();
        Assertions.assertThat(foundBook.getTitle()).isEqualTo("감정은 습관이다");
        Assertions.assertThat(foundBook.getAuthor()).isEqualTo("박용철");
    }

    @Test
    public void 책_대여() {
        Book book = bookRepository.findByTitle("세월의돌");
        Member admin = memberRepository.findByAccount("admin");

        // when
        book.loan(admin);

        // then
        Rent rent = book.getRent();

        Assertions.assertThat(book.getStatus()).isEqualTo(BookStatus.LOAN);
    }

    @Test
    public void 책_반납() {
        /**
         * A. 책: 보관상태(KEEP)
         * B. 멤버 대여 이력에 남아야 함.
         */
        // given
        Book book = bookRepository.findByTitle("세월의돌");
        Member admin = memberRepository.findByAccount("admin");
        Rent rent = book.loan(admin);

        // when
        rent._return(admin);

        // then
        Rent findedRent = admin.getRents().get(0);

        Assertions.assertThat(book.getStatus()).isEqualTo(BookStatus.KEEP);
        Assertions.assertThat(findedRent).isEqualTo(rent);
    }

    @Test
    public void 책_예약() {
        /**
         * A. 책: 예약상태(RESERVED)
         * B. 멤버 예약 내역에 남아야 함.
         */
        // given
        Book book = bookRepository.findByTitle("세월의돌");
        Member admin = memberRepository.findByAccount("admin");
        Member guest = memberRepository.findByAccount("guest");
        book.loan(guest);

        // when
        Reservation reservation = book.reserve(admin);

        // then
        Reservation findedReservation = admin.getReservations().get(0);

        Assertions.assertThat(book.getStatus()).isEqualTo(BookStatus.RESERVED);
        Assertions.assertThat(reservation).isEqualTo(findedReservation);
    }

    @Test
    public void 책_예약_취소() {
        /**
         *  A. 책: 대여상태(LOAN)
         *  B. 예약 내역에 남지 않아야 함.
         */
        // given
        Book book = bookRepository.findByTitle("세월의돌");
        Member admin = memberRepository.findByAccount("admin");
        Member guest = memberRepository.findByAccount("guest");
        book.loan(admin);
        Reservation reservation = book.reserve(guest);

        // when
        reservation.cancel();

        // then
        List<Reservation> reservations = guest.getReservations();

        Assertions.assertThat(book.getStatus()).isEqualTo(BookStatus.LOAN);
        Assertions.assertThat(reservations.size()).isEqualTo(0);
    }

}