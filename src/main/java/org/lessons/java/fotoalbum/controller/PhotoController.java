package org.lessons.java.fotoalbum.controller;

import jakarta.validation.Valid;
import org.lessons.java.fotoalbum.model.Photo;
import org.lessons.java.fotoalbum.repository.PhotoRepository;
import org.lessons.java.fotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoService photoService;

    @GetMapping
    public String index(Model model) {
        List<Photo> photos = photoRepository.findAll();
        model.addAttribute("list", photos);
        return "photos/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {


        try {
            Photo photo = photoService.findById(id);
            model.addAttribute("photo", photo);
            return "photos/show";

        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Photo not found");

        }
    }

    @GetMapping("/search")
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

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("photo", new Photo());
        return "/photos/create";
    }

    @PostMapping("/create")
    public String doCreate(@Valid @ModelAttribute("photo") Photo formPhoto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "photos/create";
        }

        return "redirect:/photos";
    }

//    @PostMapping("/create")
//    public String doCreate(@Validated @ModelAttribute("photo") Photo formPhoto, BindingResult bindingResult, Model model) {
//        //Validazione
//        boolean hasErrors = bindingResult.hasErrors();
//        //custom name validation
//        if (!pizzaService.validName(formPizza)) {
//            //aggiungo un errore al binding result
//            bindingResult.addError(new FieldError("pizza", "name", formPizza.getName(), false, null, null, "Esiste già una pizza con questo nome"));
//            hasErrors = true;
//        }
//        if (hasErrors) {
//            //ritorno la view con il form
//            model.addAttribute("ingredientList", ingredientService.getAll());
//            return "pizzas/create";
//        }
//        //se non ci sono errori lo persisto
//        pizzaService.createPizza(formPizza);
//        return "redirect:/pizzas";
//    }
}
