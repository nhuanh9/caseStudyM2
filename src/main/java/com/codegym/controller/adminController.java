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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class adminController {

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

    @GetMapping("/admin")
    public ModelAndView homeGuest(){
        ModelAndView modelAndView = new ModelAndView("admin/home");
        modelAndView.addObject("user",getPrincipal());
        return modelAndView;

    }

    @GetMapping("/admin/add-new-user")
    public ModelAndView createUser(){
        ModelAndView modelAndView = new ModelAndView("admin/user/create");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }
    @PostMapping("/admin/add-new-user")
    public ModelAndView addUser(@ModelAttribute("user") User user){
        userService.save(user);
        ModelAndView modelAndView = new ModelAndView("admin/user/create");
        modelAndView.addObject("user", user);
        modelAndView.addObject("message", "Created new user successfully");
        return modelAndView;
    }

    @GetMapping("/admin/users")
    public ModelAndView userList(Pageable pageable){
        Page<User> users = userService.findAll(pageable);
        ModelAndView modelAndView = new ModelAndView("admin/user/list");
        modelAndView.addObject("users", users);
        return modelAndView;
    }

}
