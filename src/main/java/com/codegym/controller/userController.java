package com.codegym.controller;

import com.codegym.model.Note;
import com.codegym.model.Type;
import com.codegym.model.User;
import com.codegym.service.NoteService;
import com.codegym.service.TypeService;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class userController {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private NoteService noteService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private UserService userService;

    @ModelAttribute("types")
    public Iterable<Type> provinces(Pageable pageable){
        return typeService.findAll(pageable);
    }

    private String getPrincipal() {
        String userName;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @GetMapping("/user")
    public ModelAndView homeGuest(){
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("user",getPrincipal());
        return modelAndView;

    }

    @GetMapping("/user/add-new-type")
    public ModelAndView createType(){
        ModelAndView modelAndView = new ModelAndView("user/type/create");
        modelAndView.addObject("type",new Type());
        return modelAndView;
    }
    @GetMapping("/user/add-new-note")
    public ModelAndView createNote(){
        ModelAndView modelAndView = new ModelAndView("user/note/create");
        modelAndView.addObject("note", new Note());
        return modelAndView;
    }
    @PostMapping("/user/add-new-type")
    public ModelAndView addType(@ModelAttribute("type") Type type){
        typeService.save(type);
        ModelAndView modelAndView = new ModelAndView("user/type/create");
        modelAndView.addObject("type", type);
        modelAndView.addObject("message", "Created new type successfully");
        return modelAndView;
    }

    @PostMapping("/user/add-new-note")
    public ModelAndView saveNoteType(@ModelAttribute("note") Note note) {
        noteService.save(note);

        ModelAndView modelAndView = new ModelAndView("/user/note/create");
        modelAndView.addObject("note", new Note());
        modelAndView.addObject("message", "Created!");
        return modelAndView;
    }
    @GetMapping("/user/types")
    public ModelAndView typeList(Pageable pageable){
        Page<Type> types = typeService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("user/type/list");
        modelAndView.addObject("types", types);
        return modelAndView;
    }

    @GetMapping("/user/notes")
    public ModelAndView showNoteList(Pageable pageable, @RequestParam("search") Optional<String> search) {
        Page<Note> notes;

        if (search.isPresent()) {
            notes = noteService.findNoteByTitleContains(search.get(), pageable);
        } else {
            notes = noteService.findAll(pageable);
        }

        ModelAndView modelAndView = new ModelAndView("/user/note/list");
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }

    @GetMapping("/user/editNote/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note != null) {
            ModelAndView modelAndView = new ModelAndView("user/note/edit");
            modelAndView.addObject("note", note);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/user/editNote")
    public ModelAndView editNoteType(@ModelAttribute Note note) {
        noteService.save(note);

        ModelAndView modelAndView = new ModelAndView("user/note/edit");
        modelAndView.addObject("note", note);
        modelAndView.addObject("message", "Updated!");
        return modelAndView;
    }

    @GetMapping("/user/deleteNote/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Note note = noteService.findById(id);
        if (note != null) {
            ModelAndView modelAndView = new ModelAndView("user/note/delete");
            modelAndView.addObject("note", note);
            return modelAndView;
        }
        return new ModelAndView("/error-404");
    }

    @PostMapping("/user/deleteNote")
    public String deleteNoteType(@ModelAttribute Note note) {
        noteService.remove(note.getId());
        return "redirect:/user/notes";
    }

}
