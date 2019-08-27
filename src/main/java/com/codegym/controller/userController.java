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
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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

    @GetMapping("/user/types")
    public ModelAndView typeList(Pageable pageable){
        Page<Type> types = typeService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("user/type/list");
        modelAndView.addObject("types", types);
        return modelAndView;
    }
}
