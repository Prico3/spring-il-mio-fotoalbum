package org.lessons.java.fotoalbum.api;


import jakarta.validation.Valid;
import org.lessons.java.fotoalbum.model.Photo;
import org.lessons.java.fotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("api/v1/photos")
public class PhotoRestController {

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public List<Photo> list(@RequestParam(name = "q") Optional<String> search) {
        if (search.isPresent()) {
            return photoService.getFilteredPhotos(search.get());
        }
        return photoService.getAllPhotos();
    }

    @GetMapping("/{id}")
    public Photo getById(@PathVariable Integer id) {
        try {
            return photoService.getById(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    //Create
    @PostMapping
    public Photo create(@Valid @RequestBody Photo photo) {
        if (!photoService.validName(photo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "{\"errors\": \"the name must be unique\"}");
        }
        return photoService.createPhoto(photo);
    }

    //update
    @PutMapping("/{id}")
    public Photo update(@PathVariable Integer id, @Valid @RequestBody Photo photo) {
        if (!photoService.validName(photo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "{\"errors\": \"the isbn must be unique\"}");
        }
        try {
            return photoService.updatePhoto(photo, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    // delete
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        try {
            boolean success = photoService.deleteById(id);
            if (!success) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Unable to delete book because it has borrowings");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
