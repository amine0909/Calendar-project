package config;

import com.businessanddecisions.middlewares.*;
import org.businessanddecisions.common.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.springframework.boot.web.servlet.FilterRegistrationBean.REQUEST_WRAPPER_FILTER_MAX_ORDER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.businessanddecisions.middlewares","org.businessanddecisions.common"})
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {

    @Autowired
    private AppRoutes appRoutes;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;


    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(encoder());
    }

    @Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }


    @Bean
    public FilterRegistrationBean adminMiddlewareBean() throws Exception {
        Filter adminMiddleware  = new AdminMiddleware();
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(adminMiddleware);
        String [] adminRoutesArray = this.appRoutes.getAdminRoutesArray();
        frb.addUrlPatterns(adminRoutesArray);
        if(adminRoutesArray.length ==0) {
            frb.setEnabled(false);
        }
        return frb;
    }

    @Bean
    FilterRegistrationBean managerMiddlewareBean() throws Exception {
        Filter managerMiddleware = new ManagerMiddleware();
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(managerMiddleware);
        String [] managerRoutesArray = this.appRoutes.getManagerRoutesArray();
        frb.addUrlPatterns(managerRoutesArray);
        if(managerRoutesArray.length ==0) {
            frb.setEnabled(false);
        }
        return frb;
    }

    @Bean
    FilterRegistrationBean employeeMiddlewareBean() throws Exception {
        Filter employeeMiddleware = new EmployeeMiddleware();
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(employeeMiddleware);
        String [] employeeRoutesArray = this.appRoutes.getEmployeeRoutesArray();
        frb.addUrlPatterns(employeeRoutesArray);
        if(employeeRoutesArray.length ==0) {
            frb.setEnabled(false);
        }
        return frb;
    }

    @Bean
    FilterRegistrationBean adminAndManagerMiddlewareBean() throws Exception {
        Filter adminAndManagerMiddleware = new AdminAndManagerMiddleware();
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(adminAndManagerMiddleware);
        String [] adminAndManagerRoutesArray = this.appRoutes.getAdminAndManagerRoutesArray();
        frb.addUrlPatterns(adminAndManagerRoutesArray);
        if(adminAndManagerRoutesArray.length == 0) {
            frb.setEnabled(false);
        }
        return frb;
    }


    @Bean
    FilterRegistrationBean allMiddleware() throws Exception {
        Filter allMiddleware = new AllMiddleware();
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(allMiddleware);
        String [] forAllRoutesArray = this.appRoutes.getRoutesForAll();
        frb.addUrlPatterns(forAllRoutesArray);

        if(forAllRoutesArray.length ==0) {
           frb.setEnabled(false);
        }
        return frb;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().
                authorizeRequests()
                .antMatchers("/token/generate-token","/testing/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(new AdminAndManagerMiddleware(),authenticationTokenFilterBean().getClass()).authorizeRequests().antMatchers("/token/generate-token").permitAll();
  }

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}