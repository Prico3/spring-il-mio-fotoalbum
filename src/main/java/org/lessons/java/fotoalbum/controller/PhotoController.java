package org.lessons.java.fotoalbum.controller;

import jakarta.validation.Valid;
import org.lessons.java.fotoalbum.model.Photo;
import org.lessons.java.fotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
public class PhotoController {
//    @Autowired
//    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;

    @GetMapping()
    public String index(Model model, @RequestParam(name = "q") Optional<String> keyword) {
        List<Photo> photos;
        if (keyword.isEmpty()) {
            photos = photoService.getAllPhotos();
        } else {
            photos = photoService.getFilteredPhotos(keyword.get());
            model.addAttribute("keyword", keyword.get());
        }
        model.addAttribute("list", photos);

        return "/photos/index";
    }


    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {


        try {
            Photo photo = photoService.getById(id);
            model.addAttribute("photo", photo);
            return "photos/show";

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found");

        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("photo", new Photo());
        return "/photos/create";
    }

//    @PostMapping("/create")
//    public String doCreate(@Valid @ModelAttribute("photo") Photo formPhoto, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "photos/create";
//        }
//        photoService.createPhoto(formPhoto);
//
//        return "redirect:/photos";
//    }

    @PostMapping("/create")
    public String doCreate(@Validated @ModelAttribute("photo") Photo formPhoto, BindingResult bindingResult, Model model) {
        //Validazione
        boolean hasErrors = bindingResult.hasErrors();
        //custom name validation
        if (!photoService.validName(formPhoto)) {
            //aggiungo un errore al binding result
            bindingResult.addError(new FieldError("photo", "title", formPhoto.getTitle(), false, null, null, "This title already exists"));
            hasErrors = true;
        }
        if (hasErrors) {
            //ritorno la view con il form
//            model.addAttribute("ingredientList", ingredientService.getAll());
            return "photos/create";
        }
        //se non ci sono errori lo persisto
        photoService.createPhoto(formPhoto);
        return "redirect:/photos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        try {
            Photo photo = photoService.getById(id);
            model.addAttribute("photo", photo);
            return "/photos/edit";
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "pizza with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Integer id, @Valid @ModelAttribute("pizza") Photo formPhoto, BindingResult bindingResult, Model model) {
        //Validazioni
        if (!photoService.validName(formPhoto)) {
            //aggiungo un errore al binding result
            bindingResult.addError(new FieldError("photo", "title", formPhoto.getTitle(), false, null, null, "This title already exists"));
        }
        if (bindingResult.hasErrors()) {
            //ricreo la view precompilata
//            model.addAttribute("ingredientList", ingredientService.getAll());
            return "/photos/edit";
        }
        //persisto la photo
        try {
            Photo updatePhoto = photoService.updatePhoto(formPhoto, id);
            return "redirect:/photos/" + Integer.toString(updatePhoto.getId());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found");
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            boolean success = photoService.deleteById(id);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "Photo deleted");

            } else {
                redirectAttributes.addFlashAttribute("message", "Unable to deleted the photo with id " + id);

//                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to delete the pizza");
            }
        } catch (Exception e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            redirectAttributes.addFlashAttribute("message", "Photo with id " + id + " not found");


        }
        return "redirect:/photos";

    }
}
