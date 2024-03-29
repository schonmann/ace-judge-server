package br.com.schonmann.acejudgeserver.core.auth.handler

import org.springframework.security.core.Authentication
import org.springframework.security.web.savedrequest.RequestCache
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.util.StringUtils


class MySavedRequestAwareAuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    private var requestCache: RequestCache = HttpSessionRequestCache()

    @Throws(ServletException::class, IOException::class)
    override fun onAuthenticationSuccess(
            request: HttpServletRequest,
            response: HttpServletResponse,
            authentication: Authentication) {

        val savedRequest = requestCache.getRequest(request, response)

        if (savedRequest == null) {
            clearAuthenticationAttributes(request)
            return
        }
        val targetUrlParam = targetUrlParameter
        if (isAlwaysUseDefaultTargetUrl || targetUrlParam != null && StringUtils.hasText(request.getParameter(targetUrlParam))) {
            requestCache.removeRequest(request, response)
            clearAuthenticationAttributes(request)
            return
        }

        clearAuthenticationAttributes(request)
    }

    fun setRequestCache(requestCache: RequestCache) {
        this.requestCache = requestCache
    }
}