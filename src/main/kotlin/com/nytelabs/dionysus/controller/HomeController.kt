package com.nytelabs.dionysus.controller

import com.nytelabs.dionysus.persistence.DrinkRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController(@Value("\${dionysus.cdnBaseUrl}") val cdnBaseUrl: String, val drinkRepository: DrinkRepository) {

    @GetMapping("/")
    fun home(model: Model): String {
        val drinks = drinkRepository.findAll(Pageable.ofSize(24))

        model.addAttribute("cdnBaseUrl", cdnBaseUrl)
        model.addAttribute("drinks", drinks)
        return "home"
    }
}
