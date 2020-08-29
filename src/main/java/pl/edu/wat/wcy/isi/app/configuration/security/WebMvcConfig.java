package pl.edu.wat.wcy.isi.app.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.edu.wat.wcy.isi.app.configuration.jwt.JwtAuthTokenFilter;
import pl.edu.wat.wcy.isi.app.configuration.jwt.JwtProvider;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthTokenFilter authenticationJwtTokenFilter(JwtProvider tokenProvider, UserDetailsServiceImpl userDetailsService) {
        return new JwtAuthTokenFilter(tokenProvider, userDetailsService);
    }
}
