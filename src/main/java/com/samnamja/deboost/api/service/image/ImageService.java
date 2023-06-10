package com.samnamja.deboost.api.service.image;

import com.samnamja.deboost.api.dto.image.response.ImageResponseDto;
import com.samnamja.deboost.api.dto.image.response.ImagesResponseDto;
import com.samnamja.deboost.api.entity.image.Image;
import com.samnamja.deboost.api.entity.image.ImageRepository;
import com.samnamja.deboost.exception.custom.CustomException;
import com.samnamja.deboost.utils.aws.AmazonS3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final AmazonS3Uploader amazonS3Uploader;

    public ImageResponseDto uploadImage(MultipartFile multipartFile) {
        String imageUrl = "";
        if (multipartFile != null && !multipartFile.isEmpty()) {
            try {
                imageUrl = amazonS3Uploader.saveFileAndGetUrl(multipartFile);
            } catch (Exception e) {
                throw CustomException.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("ㅜㅜ").build();
            }
        }
        ImageResponseDto imageResponseDto = ImageResponseDto.builder().imageUrl(imageUrl).build();
        return imageResponseDto;
    }

    public List<ImagesResponseDto> getImages() {
        List<ImagesResponseDto> imagesResponseDto = new ArrayList<>();
        List<Image>  img = imageRepository.findAll();

        Map<String, List<Image>> group = img.stream()
                .collect(Collectors.groupingBy(Image::getType));
        group.keySet().stream()
                .forEach(key -> {
                    List<String> collect = group.get(key).stream().map(Image::getImageUrl).collect(Collectors.toList());
                    imagesResponseDto.add(ImagesResponseDto.builder().imagesList(collect).type(key).build());
                });
        return imagesResponseDto;
    }

}
