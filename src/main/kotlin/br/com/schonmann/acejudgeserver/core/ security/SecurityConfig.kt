package br.com.schonmann.acejudgeserver.core.` security`

import br.com.schonmann.acejudgeserver.core.auth.RestAuthenticationEntryPoint
import br.com.schonmann.acejudgeserver.core.auth.handler.FailureHandler
import br.com.schonmann.acejudgeserver.core.auth.handler.SuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.filter.CorsFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityJavaConfig @Autowired constructor(
        private val restAuthenticationEntryPoint: RestAuthenticationEntryPoint,
        private val successHandler: SuccessHandler,
        private val failureHandler: FailureHandler

) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.inMemoryAuthentication()
                .withUser("admin").password(encoder().encode("admin")).roles("ADMIN")
                .and()
                .withUser("user").password(encoder().encode("user")).roles("USER")
    }

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
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
            .antMatchers("/api/admin/**").hasRole("ADMIN")
            .and()
            .formLogin()
            .successHandler(successHandler)
            .failureHandler(failureHandler)
            .and()
            .logout()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", CorsConfiguration().applyPermitDefaultValues())
        return source
    }
}

