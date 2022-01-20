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

    @NotNull
    @Column(name = "file_name")
    private String fileName;

    @NotNull
    @Column(name = "file_size")
    private String fileSize;

    @NotNull
    @Column(name = "file_extension")
    private String fileExtension;

    public ProfileImage(String fileName, String fileSize, String fileExtension) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }

    public static ProfileImage of(String filename, String filesize, String fileExtension){
        return new ProfileImage(filename, filesize, fileExtension);
    }

    public void changeImage(String filename, String filesize, String fileExtension){
        this.fileName = filename;
        this.fileSize = filesize;
        this.fileExtension = fileExtension;
    }

}
