package com.nytelabs.dionysus.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.csrf.CsrfException

class SecurityAccessDeniedHandler : AccessDeniedHandler {
    private val log = LoggerFactory.getLogger(SecurityAccessDeniedHandler::class.java)

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, accessDeniedException: AccessDeniedException) {
        val user = SecurityContextHolder.getContext().authentication?.name ?: "anonymous"
        log.warn("Access denied: user={} method={} path={} reason={}", user, request.method, request.requestURI, accessDeniedException.message)

        if (accessDeniedException is CsrfException) {
            response.sendRedirect(request.contextPath + "/login?expired")
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND)
        }
    }
}
