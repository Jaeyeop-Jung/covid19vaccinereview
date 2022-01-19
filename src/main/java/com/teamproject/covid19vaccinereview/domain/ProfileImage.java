package com.teamproject.covid19vaccinereview.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROFILEIMAGE")
public class ProfileImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILEIMAGE_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @NotNull
    @Column(name = "file_data")
    @Lob
    private byte[] data;

    @NotNull
    @Column(name = "file_name")
    private String name;

    @NotNull
    @Column(name = "file_size")
    private String size;

    @NotNull
    @Column(name = "file_contenttype")
    private String contentType;

    public ProfileImage(User user, byte[] data, String name, String size, String contentType) {
        this.user = user;
        this.data = data;
        this.name = name;
        this.size = String.valueOf(data.length);
        this.contentType = contentType;
    }

    public static ProfileImage of(User user, byte[] data, String name, String size, String contentType){
        return new ProfileImage(user, data, name, size, contentType);
    }

    public void changeImage(byte[] data ,String name, String size, String contentType){
        this.data = data;
        this.name = name;
        this.size = String.valueOf(data.length);
        this.contentType = contentType;
    }

}
