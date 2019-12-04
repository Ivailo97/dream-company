package dreamcompany.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {

    String[] uploadImage(MultipartFile multipartFile);

    void deleteImage(String imageId);
}
