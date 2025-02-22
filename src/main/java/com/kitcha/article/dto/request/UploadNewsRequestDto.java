package com.kitcha.article.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadNewsRequestDto {
    private MultipartFile imageFile;  // 업로드할 이미지 파일
}
