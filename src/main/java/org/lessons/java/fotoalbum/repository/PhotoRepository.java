package org.lessons.java.fotoalbum.repository;

import org.lessons.java.fotoalbum.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    public List<Photo> findByTitleContainingIgnoreCase(String title);
}
