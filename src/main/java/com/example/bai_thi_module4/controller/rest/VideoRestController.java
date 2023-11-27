package com.example.bai_thi_module4.controller.rest;


import com.example.bai_thi_module4.service.book.BookService;
import com.example.bai_thi_module4.service.book.request.BookSaveRequest;
import com.example.bai_thi_module4.service.book.response.VideoDetailResponse;
import com.example.bai_thi_module4.service.book.response.VideoListResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/videos")
@AllArgsConstructor
public class VideoRestController {

    private final BookService bookService;

    @PostMapping
    public void create(@RequestBody BookSaveRequest request){
        bookService.create(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDetailResponse> findById(@PathVariable Long id){
        return new ResponseEntity<>(bookService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<VideoListResponse>> getVideos(@PageableDefault(size = 5) Pageable pageable,
                                                             @RequestParam(defaultValue = "") String search) {
        return new ResponseEntity<>(bookService.getVideos(pageable, search), HttpStatus.OK);
    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateVideo(@RequestBody BookSaveRequest request, @PathVariable Long id){
        bookService.update(request,id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Boolean isDeleted = bookService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
