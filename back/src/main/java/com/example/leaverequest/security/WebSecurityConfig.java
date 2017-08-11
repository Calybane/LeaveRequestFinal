package com.example.leaverequest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    private String userRole;
    
    private String userLogin;
    
    private String userPassword;
    
    private String adminRole;
    
    private String adminLogin;
    
    private String adminPassword;
    
    private String managerLogin;
    
    private String managerPassword;
    
    private String managerRole;
    
    private String hrLogin;
    
    private String hrPassword;
    
    private String hrRole;
    
    @Value("${app.auth.user.role}")
    public void setUserRole(String userRole)
    {
        this.userRole = userRole;
    }
    
    @Value("${app.auth.user.login}")
    public void setUserLogin(String userLogin)
    {
        this.userLogin = userLogin;
    }
    
    @Value("${app.auth.user.password}")
    public void setUserPassword(String userPassword)
    {
        this.userPassword = userPassword;
    }
    
    @Value("${app.auth.admin.role}")
    public void setAdminRole(String adminRole)
    {
        this.adminRole = adminRole;
    }
    
    @Value("${app.auth.admin.login}")
    public void setAdminLogin(String adminLogin)
    {
        this.adminLogin = adminLogin;
    }
    
    @Value("${app.auth.admin.password}")
    public void setAdminPassword(String adminPassword)
    {
        this.adminPassword = adminPassword;
    }
    
    @Value("${app.auth.manager.login}")
    public void setManagerLogin(String managerLogin)
    {
        this.managerLogin = managerLogin;
    }
    
    @Value("${app.auth.manager.password}")
    public void setManagerPassword(String managerPassword)
    {
        this.managerPassword = managerPassword;
    }
    
    @Value("${app.auth.manager.role}")
    public void setManagerRole(String managerRole)
    {
        this.managerRole = managerRole;
    }
    
    @Value("${app.auth.hr.login}")
    public void setHrLogin(String hrLogin)
    {
        this.hrLogin = hrLogin;
    }
    
    @Value("${app.auth.hr.password}")
    public void setHrPassword(String hrPassword)
    {
        this.hrPassword = hrPassword;
    }
    
    @Value("${app.auth.hr.role}")
    public void setHrRole(String hrRole)
    {
        this.hrRole = hrRole;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().fullyAuthenticated()
            .antMatchers("/").permitAll()
            .and()
            // We filter the api/login requests
            .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                    UsernamePasswordAuthenticationFilter.class)
            // And filter other requests to check the presence of JWT in header
            .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser(userLogin).password(userPassword)
                .roles(userRole)
                .authorities(userRole)
            .and()
            .withUser(managerLogin).password(managerPassword)
                .roles(userRole, managerRole)
                .authorities(userRole, managerRole)
            .and()
            .withUser(hrLogin).password(hrPassword)
                .roles(userRole, hrRole)
                .authorities(userRole, managerRole, hrRole)
            .and()
            .withUser(adminLogin).password(adminPassword)
                .roles(userRole, adminRole, managerRole, hrRole)
                .authorities(userRole, adminRole, managerRole, hrRole);
    }
}
