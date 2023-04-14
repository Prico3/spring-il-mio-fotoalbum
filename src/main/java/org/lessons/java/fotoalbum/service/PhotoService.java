package org.lessons.java.fotoalbum.service;

import org.lessons.java.fotoalbum.model.Photo;
import org.lessons.java.fotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {
    @Autowired
    PhotoRepository photoRepository;

    public Photo createPhoto(Photo formPhoto) {
        Photo photoToPersist = new Photo();
        photoToPersist.setTitle(formPhoto.getTitle());
        photoToPersist.setDescription(formPhoto.getDescription());
        photoToPersist.setImgPath(formPhoto.getImgPath());
        photoToPersist.setCreatedAt(LocalDateTime.now());
        return photoRepository.save(formPhoto);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll(Sort.by("title"));

    }

    public List<Photo> getFilteredPhotos(String keyword) {

        return photoRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Photo findById(Integer id) throws RuntimeException {
        Optional<Photo> result = photoRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Photo not found");
        }
    }

}
