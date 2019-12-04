package dreamcompany.service.implementation;

import com.cloudinary.Cloudinary;
import dreamcompany.service.interfaces.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public String[] uploadImage(MultipartFile multipartFile) {

        File file = null;

        try {
            file = File.createTempFile("temp-file", multipartFile.getOriginalFilename());

            multipartFile.transferTo(file);
        } catch (Exception ignored) {
        }

        Map uploadedData = null;

        try {
            uploadedData = cloudinary.uploader().upload(file, new HashMap());
        } catch (Exception ignored) {
        }


        return new String[]{uploadedData.get("url").toString(), uploadedData.get("public_id").toString()};
    }

    @Override
    public void deleteImage(String imageId) {

        try {
            cloudinary.uploader().destroy(imageId, new HashMap());
        } catch (Exception ignored) {

        }
    }
}
