package dreamcompany.service.implementation;

import com.cloudinary.Cloudinary;
import dreamcompany.service.interfaces.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String[] uploadImage(MultipartFile multipartFile) throws IOException {

        File file = File.createTempFile("temp-file", multipartFile.getOriginalFilename());

        multipartFile.transferTo(file);

        Map uploadedData = cloudinary.uploader().upload(file, new HashMap());

        return new String[]{uploadedData.get("url").toString(), uploadedData.get("public_id").toString()};
    }

    @Override
    public void deleteImage(String imageId) throws IOException {
        cloudinary.uploader().destroy(imageId, new HashMap());
    }
}
