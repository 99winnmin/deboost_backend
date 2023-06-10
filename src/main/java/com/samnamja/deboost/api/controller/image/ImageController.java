package com.samnamja.deboost.api.controller.image;

import com.samnamja.deboost.api.dto.image.request.ImageRequestDto;
import com.samnamja.deboost.api.dto.image.response.ImageResponseDto;
import com.samnamja.deboost.api.dto.image.response.ImagesResponseDto;
import com.samnamja.deboost.api.entity.image.Image;
import com.samnamja.deboost.api.entity.user.detail.UserAccount;
import com.samnamja.deboost.api.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image")
    public ResponseEntity<ImageResponseDto> uploadImage
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @RequestPart MultipartFile multipartFile) {
        ImageResponseDto imageUrl = imageService.uploadImage(multipartFile);
        return ResponseEntity.ok().body(imageUrl);
    }


    @GetMapping("/images")
    public ResponseEntity<List<ImagesResponseDto>> getImages(@AuthenticationPrincipal UserAccount userAccount) {
        List<ImagesResponseDto> result = imageService.getImages();
        return ResponseEntity.ok().body(result);
    }
}
