package com.example.bai_thi_module4.service.book;

import com.example.bai_thi_module4.model.Author;
import com.example.bai_thi_module4.model.Book;
import com.example.bai_thi_module4.model.BookAuthor;
import com.example.bai_thi_module4.repository.BookAuthorRepository;
import com.example.bai_thi_module4.repository.BookRepository;
import com.example.bai_thi_module4.service.book.request.BookSaveRequest;
import com.example.bai_thi_module4.service.book.response.VideoDetailResponse;
import com.example.bai_thi_module4.service.book.response.VideoListResponse;
import com.example.bai_thi_module4.util.AppUtil;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final BookAuthorRepository bookAuthorRepository;

    public void create(BookSaveRequest request){
        var book = AppUtil.mapper.map(request, Book.class);
        book = bookRepository.save(book);
        Book finalBook = book;
        bookAuthorRepository.saveAll(request
                .getIdPlaylists()
                .stream()
                .map(id -> new BookAuthor(finalBook, new Author(Long.valueOf(id))))
                .collect(Collectors.toList()));
    }

    public VideoDetailResponse findById(Long id){
        var video = bookRepository.findById(id).orElse(new Book());

        var result = AppUtil.mapper.map(video, VideoDetailResponse.class);
        result.setPlaylistIds(video
                .getBookAuthors()
                .stream().map(videoPlaylist -> videoPlaylist.getAuthor().getId())
                .collect(Collectors.toList()));
        return result;
    }

    public Page<VideoListResponse> getVideos(Pageable pageable, String search){
        search = "%" + search + "%";
        return bookRepository.searchEverything(search ,pageable).map(e -> {
            var result = AppUtil.mapper.map(e, VideoListResponse.class);
            result.setVideoPlaylist(e.getBookAuthors()
                    .stream().map(c -> c.getAuthor().getName())
                    .collect(Collectors.joining(", ")));
            return result;
        });
    }

    public void update(BookSaveRequest request, Long id){
        var videoDb = bookRepository.findById(id).orElse(new Book());
        AppUtil.mapper.map(request,videoDb);
        bookAuthorRepository.deleteAll(videoDb.getBookAuthors());


        var videoPlaylists = new ArrayList<BookAuthor>();
        for (String idPlaylist : request.getIdPlaylists()) {
            Author playlist = new Author(Long.valueOf(idPlaylist));
            videoPlaylists.add(new BookAuthor(videoDb, playlist));
        }
        bookAuthorRepository.saveAll(videoPlaylists);
        bookRepository.save(videoDb);
    }
    public Boolean delete(Long id) {
        bookAuthorRepository.deleteBookAuthorByBook_Id(id);
        bookRepository.deleteById(id);
        return true;
    }
}
