package com.example.bai_thi_module4.repository;


import com.example.bai_thi_module4.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT v FROM Book v " +
            "WHERE " +
            "v.title LIKE :search OR " +
            "v.description LIKE :search OR " +
            "EXISTS (SELECT 1 FROM BookAuthor vp WHERE vp.book = v AND vp.author.name LIKE :search)")


    Page<Book> searchEverything(String search, Pageable pageable);
}
