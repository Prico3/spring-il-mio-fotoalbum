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
        photoToPersist.setCategories(formPhoto.getCategories());
        return photoRepository.save(formPhoto);
    }

    public Photo updatePhoto(Photo formPhoto, Integer id) throws RuntimeException {
        Photo photoToUpdate = getById(id);
        photoToUpdate.setTitle(formPhoto.getTitle());
        photoToUpdate.setDescription(formPhoto.getDescription());
        photoToUpdate.setImgPath(formPhoto.getImgPath());
        photoToUpdate.setVisible(formPhoto.isVisible());
        photoToUpdate.setCategories(formPhoto.getCategories());

        return photoRepository.save(photoToUpdate);
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll(Sort.by("title"));

    }

    public List<Photo> getFilteredPhotos(String keyword) {

        return photoRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public Photo getById(Integer id) throws RuntimeException {
        Optional<Photo> result = photoRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Photo not found");
        }
    }

    public boolean deleteById(Integer id) {
        photoRepository.findById(id).orElseThrow(() -> new RuntimeException(Integer.toString(id)));
        try {
            photoRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validName(Photo photoToValidate) {
        //verifico se su sul DB esistte una photo con lo stesso nome di photoToValidate
        if (photoToValidate.getId() == null) {
            return !photoRepository.existsByTitle(photoToValidate.getTitle());
        }
        return !photoRepository.existsByTitleAndIdNot(photoToValidate.getTitle(), photoToValidate.getId());
    }


}
