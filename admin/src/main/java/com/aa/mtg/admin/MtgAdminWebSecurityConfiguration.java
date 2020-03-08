package com.aa.mtg.admin;

import com.aa.mtg.admin.auth.MtgLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class MtgAdminWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
  @Autowired
  private MtgLogoutSuccessHandler mtgLogoutSuccessHandler;

  @Override
  public void configure(WebSecurity web) {
    web
      .ignoring()
      .antMatchers("/js/**", "/img/**", "/admin.css");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .sessionManagement(cust -> cust.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .logout(cust -> {
        cust.logoutUrl("/auth/logout");
        cust.addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL)));
        cust.logoutSuccessHandler(mtgLogoutSuccessHandler);
      })
      .authorizeRequests()
      .antMatchers("/", "/ui/**", "/auth/**").permitAll()
      .anyRequest().authenticated();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    String defaultEncodingId = "argon2";
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(defaultEncodingId, new Argon2PasswordEncoder(16, 32, 8, 1 << 16, 4));
    return new DelegatingPasswordEncoder(defaultEncodingId, encoders);
  }

  @Bean
  @Override
  protected AuthenticationManager authenticationManager() {
    return authentication -> {
      throw new AuthenticationServiceException("Cannot authenticate " + authentication);
    };
  }
}
