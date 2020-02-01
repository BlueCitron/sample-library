package com.bluecitron.library.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    private Integer bookCount;

    @OneToMany(mappedBy = "category")
    private List<Book> books = new ArrayList<>();

    public Category(String name) {
        this.name = name;
        this.bookCount = 0;
    }

    public Book addBook(Book book) {
        book.setCategory(this);
        this.books.add(book);
        this.bookCount += 1;
        return book;
    }

    public void removeBook(Book book) {
        book.setCategory(null);
        this.books.removeIf(_book -> _book == book);
        this.bookCount -= 1;
    }
}
