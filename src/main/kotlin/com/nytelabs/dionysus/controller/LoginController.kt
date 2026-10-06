package com.nytelabs.dionysus.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {
    companion object {
        private val logoutHandler = SecurityContextLogoutHandler()
    }

    @GetMapping("/login")
    fun login(request: HttpServletRequest): String {
        if (request.getParameter("logout") != null) {
            return "redirect:/logout"
        }

        return "login"
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse) {
        val auth: Authentication? = SecurityContextHolder.getContext().authentication
        logoutHandler.logout(request, response, auth)
        response.sendRedirect("/login")
    }
}