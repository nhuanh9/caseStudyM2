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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class guestController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private NoteService noteService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private UserService userService;

    @GetMapping("/home")
    public ModelAndView homeGuest(){
        ModelAndView modelAndView = new ModelAndView("/guest/home");
        modelAndView.addObject("user",getPrincipal());
        return modelAndView;

    }

    @GetMapping("/login")
    public ModelAndView loginForm() {
        return new ModelAndView("guest/login");
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

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("ssoId") String username, @RequestParam("password") String password) {
        ArrayList<User> users = (ArrayList<User>) userService.findAll();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                if (user.getRole().equals("admin")) {
                    return new ModelAndView("/admin/home");
                } else {
                    return new ModelAndView("/user/home");
                }
            }
        }
        ModelAndView modelAndView = new ModelAndView("guest/login");
        modelAndView.addObject("message", "username or password incorrect");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView showRegisterForm() {
        ModelAndView modelAndView = new ModelAndView("/guest/register");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerNewUser(@Valid @ModelAttribute User user, BindingResult bindingResult) {


        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/guest/register");
            return modelAndView;
        }
        User currentUser = new User();

        currentUser.setUsername(user.getUsername());
        currentUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(currentUser);

        ModelAndView modelAndView = new ModelAndView("/guest/login");
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    @GetMapping("/notes")
    public ModelAndView showNoteList(Pageable pageable, @RequestParam("search") Optional<String> search) {
        Page<Note> notes;

        if (search.isPresent()) {
            notes = noteService.findNoteByTitleContains(search.get(), pageable);
        } else {
            notes = noteService.findAll(pageable);
        }

        ModelAndView modelAndView = new ModelAndView("/guest/note/list");
        modelAndView.addObject("notes", notes);
        return modelAndView;
    }

    @GetMapping("/types")
    public ModelAndView typeList(Pageable pageable){
        Page<Type> types = typeService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("guest/type/list");
        modelAndView.addObject("types", types);
        return modelAndView;
    }
}
