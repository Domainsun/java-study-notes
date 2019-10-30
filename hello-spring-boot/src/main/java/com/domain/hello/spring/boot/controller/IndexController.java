package com.domain.hello.spring.boot.controller;

import com.domain.hello.spring.boot.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String index(Model model){


        List users = new ArrayList();

        for (int i = 0; i <3 ; i++) {
            users.add(new User("domain"+i,i));
        }

        model.addAttribute("users",users);



        return  "index";
    }

}
