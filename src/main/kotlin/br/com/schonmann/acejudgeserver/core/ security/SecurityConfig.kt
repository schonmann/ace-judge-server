package br.com.schonmann.acejudgeserver.core.security

import br.com.schonmann.acejudgeserver.core.auth.RestAuthenticationEntryPoint
import br.com.schonmann.acejudgeserver.core.auth.handler.MySavedRequestAwareAuthenticationSuccessHandler
import br.com.schonmann.acejudgeserver.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.stream.Collectors
import java.util.stream.Stream

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityJavaConfig @Autowired constructor(
        private val authService: AuthService,
        private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint

) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(authService).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun successHandler(): AuthenticationSuccessHandler {
        val handler = MySavedRequestAwareAuthenticationSuccessHandler()
        handler.setTargetUrlParameter("/api/user/data")
        return handler
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .antMatchers("/api_admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .successHandler(successHandler())
                .failureHandler(SimpleUrlAuthenticationFailureHandler())
                .and()
                .logout()
                .logoutSuccessHandler(SimpleUrlLogoutSuccessHandler())
                .logoutUrl("/logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val cors = CorsConfiguration().applyPermitDefaultValues();
        cors.allowCredentials = true
        cors.allowedMethods = Stream.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH").collect(Collectors.toList())
        cors.allowedHeaders = Stream.of("Authorization", "Cache-Control", "Content-Type").collect(Collectors.toList())
        cors.allowedOrigins = Stream.of("*").collect(Collectors.toList())
        source.registerCorsConfiguration("/**", cors)
        return source
    }
}

