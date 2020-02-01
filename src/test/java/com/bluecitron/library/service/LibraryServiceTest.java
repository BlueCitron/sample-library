package com.bluecitron.library.service;

import com.bluecitron.library.entity.*;
import com.bluecitron.library.repository.BookRepository;
import com.bluecitron.library.repository.RentRepository;
import com.bluecitron.library.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
class LibraryServiceTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private LibraryService libraryService;

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
    void 책_조회() {
        List<Book> books = libraryService.getBooks();

        Assertions.assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void 책_대여() {
        // given
        Book book = bookRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);

        // when
        Long rentId = libraryService.rentBook(book.getId(), member.getId());

        // then
        Rent rent = member.getRents().get(0);

        Assertions.assertThat(book.getStatus()).isEqualTo(BookStatus.LOAN);
        Assertions.assertThat(rent.getId()).isEqualTo(rentId);
    }

    @Test
    void 책_반납() {
        // given
        Book book = bookRepository.findAll().get(0);
        Member member = memberRepository.findAll().get(0);
        Long rentId = libraryService.rentBook(book.getId(), member.getId());

        // when
        libraryService.returnBook(rentId, member.getId());

        // then
        Rent rent = rentRepository.findAll().get(0);

        Assertions.assertThat(rent.getStatus()).isEqualTo(RentStatus.RETURNNED);
        Assertions.assertThat(book.getStatus()).isEqualTo((BookStatus.KEEP));
    }

    @Test
    void 책_예약() {
        // given
        Book book = bookRepository.findAll().get(0);
        Member admin = memberRepository.findAll().get(0);
        Member guest = memberRepository.findAll().get(1);
        Long rentId = libraryService.rentBook(book.getId(), admin.getId());

        // when
        Long reserveId = libraryService.reserveBook(book.getId(), guest.getId());
        Reservation reservation = guest.getReservations().get(0);

        Assertions.assertThat(book.getStatus()).isEqualTo(BookStatus.RESERVED);
        Assertions.assertThat(reservation.getId()).isEqualTo(reserveId);
    }

}