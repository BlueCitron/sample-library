package com.bluecitron.library.repository;

import com.bluecitron.library.entity.Book;
import com.bluecitron.library.entity.Category;
import com.bluecitron.library.entity.Member;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

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
    void 책_추가() {
        // given
        Category sample = new Category("Sample");
        Book book = new Book("SampleBook", "bySample", "1234-5678");
        categoryRepository.save(sample);
        bookRepository.save(book);

        // when
        sample.addBook(book);

        // then
        Assertions.assertThat(sample.getBookCount()).isEqualTo(1);
        Assertions.assertThat(book.getCategory()).isEqualTo(sample);
        Assertions.assertThat(sample.getBooks().get(0)).isEqualTo(book);
    }

    @Test
    void 책_제거() {
        // given
        Category sample = new Category("Sample");
        Book book = new Book("SampleBook", "bySample", "1234-5678");
        categoryRepository.save(sample);
        bookRepository.save(book);
        sample.addBook(book);

        // when
        sample.removeBook(book);

        // then
        Assertions.assertThat(book.getCategory()).isNull();
        Assertions.assertThat(sample.getBooks().size()).isEqualTo(0);
        Assertions.assertThat(sample.getBookCount()).isEqualTo(0);
    }


}