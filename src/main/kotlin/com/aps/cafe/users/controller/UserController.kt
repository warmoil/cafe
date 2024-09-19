package com.aps.cafe.users.controller

import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class HomeController {
    @GetMapping("/")
    fun home(model: Model): String {
        model.addAttribute("nickname", "새로운분")

        return "index"
    }
}

@RequestMapping("users")
@Controller
class UserController {
    @GetMapping("/login")
    fun gotoLogin(): String = "users/login"

    @GetMapping("/register")
    fun register(): String = "users/register"
}