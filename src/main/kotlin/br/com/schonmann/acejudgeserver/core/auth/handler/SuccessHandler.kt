package br.com.schonmann.acejudgeserver.core.auth.handler

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.security.web.savedrequest.RequestCache
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import javax.servlet.ServletException



@Component
class SuccessHandler : SimpleUrlAuthenticationSuccessHandler() {
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

