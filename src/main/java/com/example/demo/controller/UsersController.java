package com.example.demo.controller;

import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UsersController {


    private final UserService service;


    @Autowired
    public UsersController(UserService service) {
        this.service = service;
    }


    //страница со списком пользователей
    @GetMapping("/users")
    public String listFirstPage(Model model){

        List<User> listUsers = service.listAll();
        model.addAttribute("pageTitle", "Пользователи");
        model.addAttribute("listUsers", listUsers);

        return "users/users";

    }

    //возращает страницу для создания пользователя
    @GetMapping("/users/new")
    public String newUser(Model model){

        User user = new User();

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Добавление нового пользователя");
        return "users/user_form";
    }
    //постзапрос, сохраняющий пользователя
    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes){
        service.save(user);
        redirectAttributes.addFlashAttribute("message", "Пользователь сохранен");
        return "redirect:/users";
    }

    //возвращает страницу для редактирования пользователя
    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name="id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes){
        try {
            User user = service.get(id);
            model.addAttribute("user", user);
            model.addAttribute("pageTitle", "Редактирование пользователя");
            return "users/user_form";
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
            return "redirect:/users";
        }

    }
   //удаляет пользователя
    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name="id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes){
        try {
            service.delete(id);
            redirectAttributes.addFlashAttribute("message",
                    "Пользовать с ID: " + id + " успешно удален");
        }catch (UserNotFoundException ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/users";
    }




}
