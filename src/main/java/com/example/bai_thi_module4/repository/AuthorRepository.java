package com.example.bai_thi_module4.repository;


import com.example.bai_thi_module4.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
