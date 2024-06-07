package com.livk.cors

import com.livk.auto.service.annotation.SpringAutoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

/**
 * @author livk
 */
@Configuration
@SpringAutoService
open class CorsConfig {
    @Bean
    open fun corsWebFilter(): CorsWebFilter {
        val corsConfiguration = CorsConfiguration()
        val source = UrlBasedCorsConfigurationSource()
        corsConfiguration.addAllowedHeader("*")
        corsConfiguration.addAllowedMethod("*")
        corsConfiguration.setAllowedOriginPatterns(listOf("*"))
        corsConfiguration.allowCredentials = true
        source.registerCorsConfiguration("/**", corsConfiguration)
        return CorsWebFilter(source)
    }
}
