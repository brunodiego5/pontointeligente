package com.bdd.pontointeligente.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

/* desabilitei o cors, o método POST não estava autorizado, apenas GET :-(  */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class BasicSecurityConfig : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()?.
                anyRequest()?.
                authenticated()?.and()?.
                httpBasic()?.and()?.
                sessionManagement()?.
                sessionCreationPolicy(SessionCreationPolicy.STATELESS)?.and()?.
                csrf()?.disable()

    }
}