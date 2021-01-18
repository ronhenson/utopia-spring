package com.smoothstack.orchestrator.security;

import java.util.List;

import com.smoothstack.orchestrator.dao.UserDao;

import com.smoothstack.orchestrator.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	UserDao userDao;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		JwtAuthenticationFilter authenticationFilter = new JwtAuthenticationFilter(authenticationManager());
		authenticationFilter.setFilterProcessesUrl("/auth/login");
		JwtAuthorizationFilter authorizationFilter = new JwtAuthorizationFilter(authenticationManager(), userDao);
		http
			// we don't need csrf or session state since we are using JWT
			.csrf().disable()
			.cors().configurationSource(request -> {
					var cors = new CorsConfiguration();
					cors.setAllowedOriginPatterns(List.of("*"));
					cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
					cors.setAllowedHeaders(List.of("*"));
					return cors;
			})
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilter(authenticationFilter)
			.addFilter(authorizationFilter)
			.authorizeRequests()
			.antMatchers("/auth/*").permitAll()
			.antMatchers("/authenticated").authenticated()
			.antMatchers("/booking").authenticated()
			.antMatchers("/booking/{bookingId}").authenticated()
			.antMatchers("/users/*").authenticated()
			.antMatchers("/users/admin/search").hasRole(UserRole.ADMIN.name())
			.anyRequest().permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return daoAuthenticationProvider;
	}
}
