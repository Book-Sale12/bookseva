package com.bookseva.book.service;
import com.bookseva.book.entity.Book;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CloudinaryService.class);
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    public String uploadRaw(byte[] data, String filename) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(data, ObjectUtils.asMap("resource_type", "image", "public_id", filename));
        return uploadResult.get("secure_url").toString();
    }

    /**
     * Deletes an image from Cloudinary by its secure URL.
     * Extracts the public_id from the URL (everything between the upload version segment and the file extension).
     *
     * @param secureUrl the Cloudinary secure URL returned at upload time
     * @throws IOException if the Cloudinary API call fails
     */
    public void deleteImage(String secureUrl) throws IOException {
        // URL pattern: .../upload/v<version>/<public_id>.<ext>
        // Extract public_id by taking the part after the last '/' and removing the extension
        String withoutQuery = secureUrl.split("\\?")[0];
        String filename = withoutQuery.substring(withoutQuery.lastIndexOf('/') + 1);
        String publicId = filename.contains(".") ? filename.substring(0, filename.lastIndexOf('.')) : filename;
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        log.debug("Deleted Cloudinary image with publicId={}", publicId);
    }

    public CloudinaryService(final Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
}
