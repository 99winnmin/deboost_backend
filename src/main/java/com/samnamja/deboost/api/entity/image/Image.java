package com.samnamja.deboost.api.entity.image;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fairy_image_id")
    private Long id;
    private String imageUrl;
    private String type;

    @Builder
    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
