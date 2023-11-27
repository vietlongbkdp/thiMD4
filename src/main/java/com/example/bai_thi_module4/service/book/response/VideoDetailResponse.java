package com.example.bai_thi_module4.service.book.response;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@NoArgsConstructor
public class VideoDetailResponse {
    private Long id;

    private String title;

    private String description;

    private List<Long> playlistIds;

    private String url;
}
