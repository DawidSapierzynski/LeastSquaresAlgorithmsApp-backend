package pl.edu.wat.wcy.isi.app.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.edu.wat.wcy.isi.app.configuration.security.jwt.JwtAuthEntryPoint;
import pl.edu.wat.wcy.isi.app.configuration.security.jwt.JwtAuthTokenFilter;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserRole;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtAuthEntryPoint unauthorizedHandler;
    private final JwtAuthTokenFilter jwtAuthTokenFilter;

    public WebSecurityConfiguration(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,
                                    JwtAuthEntryPoint unauthorizedHandler, JwtAuthTokenFilter jwtAuthTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtAuthTokenFilter = jwtAuthTokenFilter;
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/auth/signin").permitAll()
                .antMatchers("/dataSeriesFile", "/doApproximations", "/approximationProperties", "/chooseMethod", "/download/approximation").hasAuthority(UserRole.USER.getCode())
                .antMatchers("/dataSeriesFile/all", "/approximationProperties/all", "/auth/signup", "/user").hasAuthority(UserRole.ADMIN.getCode())
                .antMatchers("/approximationProperties/{approximationPropertiesId}", "/user/{userId}", "/roleUser", "/download/generateDataSeries").hasAnyAuthority(UserRole.ADMIN.getCode(), UserRole.USER.getCode())
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    AuthenticationManager getAuthenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
