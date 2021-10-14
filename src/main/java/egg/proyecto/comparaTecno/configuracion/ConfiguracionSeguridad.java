package egg.proyecto.comparaTecno.configuracion;

import egg.proyecto.comparaTecno.error.ExceptionCreados;
import egg.proyecto.comparaTecno.servicio.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**

 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class ConfiguracionSeguridad extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
           
            auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
		
	}
        
        @Override 
    protected void configure(HttpSecurity http) throws ExceptionCreados, Exception {
        http
                .authorizeRequests()
                    .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
                    .and().formLogin()
                        .loginPage("/login")//nombre de html login
                            .loginProcessingUrl("/logincheck")
                            .usernameParameter("mail")
                            .passwordParameter("password")
                            .defaultSuccessUrl("/loginsuccess")
//                            .failureForwardUrl("/login?error=error")
                            .permitAll()
                    .and().logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                    .and().csrf()
                        .disable();
    }

}
