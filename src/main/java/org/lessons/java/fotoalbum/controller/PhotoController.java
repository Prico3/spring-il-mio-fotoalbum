package org.lessons.java.fotoalbum.controller;

import org.lessons.java.fotoalbum.model.Photo;
import org.lessons.java.fotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/photos")
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping
    public String index(Model model) {
        List<Photo> photos = photoRepository.findAll();
        model.addAttribute("list", photos);
        return "photos/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Integer id, Model model) {

        Optional<Photo> result = photoRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("photo", result.get());
            return "/photos/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/search")
//    public String index(Model model, @RequestParam(name = "q") Optional<String> keyword) {
//        List<Photo> pizzas;
//        if (keyword.isEmpty()) {
//            photos = photoService.getAllPizzas();
//        } else {
//            photos = pizzaService.getFilteredPizzas(keyword.get());
//            model.addAttribute("keyword", keyword.get());
//        }
//        model.addAttribute("list", photos);
//
//        return "/pizzas/index";
//    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("photo", new Photo());
        return "/photos/create";
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
