package pl.edu.wat.wcy.isi.app.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.edu.wat.wcy.isi.app.configuration.security.jwt.JwtAuthEntryPoint;
import pl.edu.wat.wcy.isi.app.configuration.security.jwt.JwtAuthTokenFilter;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserRole;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService, PasswordEncoder bCryptPasswordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthEntryPoint unauthorizedHandler, JwtAuthTokenFilter jwtAuthTokenFilter) throws Exception {
        http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/auth/signin").permitAll()
                .requestMatchers("/dataSeriesFile", "/doApproximations", "/approximationProperties", "/chooseMethod", "/download/approximation").hasAuthority(UserRole.USER.getCode())
                .requestMatchers("/dataSeriesFile/all", "/approximationProperties/all", "/auth/signup", "/user").hasAuthority(UserRole.ADMIN.getCode())
                .requestMatchers("/approximationProperties/{approximationPropertiesId}", "/user/{userId}", "/roleUser", "/download/generateDataSeries").hasAnyAuthority(UserRole.ADMIN.getCode(), UserRole.USER.getCode())
                .anyRequest().authenticated());
        http.exceptionHandling((exceptionHandling) -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler));
        http.sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
