package com.bookseva.book.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
=======
import lombok.extern.slf4j.Slf4j;
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

<<<<<<< HEAD
=======
@Slf4j
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return uploadResult.get("secure_url").toString();
    }

    public String uploadRaw(byte[] data, String filename) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(data, ObjectUtils.asMap("resource_type", "image", "public_id", filename));
        return uploadResult.get("secure_url").toString();
    }
<<<<<<< HEAD
=======

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
>>>>>>> 7fb50eb898dfa7106f0876c78aeafb3c656106d2
}
