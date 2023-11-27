package com.example.bai_thi_module4.service.author;

import com.example.bai_thi_module4.repository.AuthorRepository;
import com.example.bai_thi_module4.service.response.SelectOptionResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;


    public List<SelectOptionResponse> findAll(){
        return authorRepository.findAll().stream()
                .map(playlist -> new SelectOptionResponse(playlist.getId().toString(), playlist.getName()))
                .collect(Collectors.toList());
    }

}
