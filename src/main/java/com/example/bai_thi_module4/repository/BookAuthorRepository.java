package com.example.bai_thi_module4.repository;

import com.example.bai_thi_module4.model.BookAuthor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    @Transactional
    void deleteBookAuthorByBook_Id(Long id);
}
