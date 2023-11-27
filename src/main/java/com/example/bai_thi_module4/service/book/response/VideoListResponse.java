package com.example.bai_thi_module4.service.book.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VideoListResponse {
    private Long id;

    private String title;

    private String description;

    private String videoPlaylist;

}
