package com.aps.cafe.users.controller

import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserController {
    @GetMapping("/")
    fun home( model: Model): String {
            model.addAttribute("nickname", "새로운분")

        return "index"
    }
}