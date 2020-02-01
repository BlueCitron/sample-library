package com.bluecitron.library.service;

import com.bluecitron.library.entity.Book;
import com.bluecitron.library.entity.Rent;
import com.bluecitron.library.entity.Member;
import com.bluecitron.library.entity.Reservation;
import com.bluecitron.library.repository.BookRepository;
import com.bluecitron.library.repository.RentRepository;
import com.bluecitron.library.repository.MemberRepository;
import com.bluecitron.library.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class LibraryService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final RentRepository rentRepository;
    private final ReservationRepository reservationRepository;

    // 1. 책 조회
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    // 2. 책 대여
    public Long rentBook(Long bookId, Long memberId) {
        Optional<Member> optMember = memberRepository.findById(memberId);
        Optional<Book> optBook = bookRepository.findById(bookId);

        if (optBook.isPresent() && optMember.isPresent()) {
            Book book = optBook.get();
            Member member = optMember.get();
            Rent rent = book.loan(member);
            rentRepository.save(rent);
            return rent.getId();
        } else {
            throw new RuntimeException();
        }
    }

    // 3. 책 예약
    public Long reserveBook(Long bookId, Long memberId) {
        Optional<Member> optMember = memberRepository.findById(memberId);
        Optional<Book> optBook = bookRepository.findById(bookId);

        if (optBook.isPresent() && optMember.isPresent()) {
            Book book = optBook.get();
            Member member = optMember.get();
            Reservation reserve = book.reserve(member);
            reservationRepository.save(reserve);
            return reserve.getId();
        } else {
            throw new RuntimeException();
        }
    }

    // 4. 책 반납
    public Long returnBook(Long loanId, Long memberId) {
        Optional<Member> optMember = memberRepository.findById(memberId);
        Optional<Rent> optLoan = rentRepository.findById(loanId);

        if (optLoan.isPresent() && optMember.isPresent()) {
            Rent rent = optLoan.get();
            Member member = optMember.get();
            rent._return(member);
            return rent.getId();
        } else {
            throw new RuntimeException();
        }
    }
}
