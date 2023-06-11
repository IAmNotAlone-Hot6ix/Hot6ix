package com.hotsix.iAmNotAlone.global.infra;

import com.hotsix.iAmNotAlone.global.util.S3UploadService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3UploadService s3UploadService;

    @PostMapping("/api/uploads")
    public ResponseEntity<Object> uploadFiles(
        @RequestParam(value = "fileType") String fileType,
        @RequestPart(value = "files") List<MultipartFile> multipartFiles) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(s3UploadService.uploadFiles(fileType, multipartFiles));
    }

    @DeleteMapping("/api/delete")
    public ResponseEntity<Object> deleteFile(
        @RequestParam(value = "uploadFilePath") String uploadFilePath,
        @RequestParam(value = "uuidFileName") String uuidFileName) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(s3UploadService.deleteFile(uploadFilePath, uuidFileName));
    }

}
