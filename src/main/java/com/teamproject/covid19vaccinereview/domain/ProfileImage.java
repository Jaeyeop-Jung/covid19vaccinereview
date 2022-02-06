package com.teamproject.covid19vaccinereview.domain;

import com.teamproject.covid19vaccinereview.dto.JoinRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "PROFILEIMAGE", uniqueConstraints = @UniqueConstraint(columnNames = {"file_name"}))
public class ProfileImage extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILEIMAGE_ID")
    private Long id;

    @NotNull
    @Column(name = "file_name")
    private String fileName;

    @NotNull
    @Column(name = "file_size")
    private Long fileSize;

    @NotNull
    @Column(name = "file_extension")
    private String fileExtension;

    public ProfileImage(String fileName, Long fileSize, String fileExtension) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }

    public static ProfileImage of(String fileName, Long fileSize, String fileExtension){
        return new ProfileImage(fileName, fileSize, fileExtension);
    }

    public void changeImage(String fileName, Long fileSize, String fileExtension){
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }
}
