package github.under_sin.libraryapi.config;

import github.under_sin.libraryapi.security.CustomUserDetailsService;
import github.under_sin.libraryapi.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // configurando o sprig security para voltar a ficar como ele já vem por default
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // como não estamos trabalhando com uma aplicação web, podemos desabilitar o csrf
                .formLogin(Customizer.withDefaults()) // default login authenticated
                .httpBasic(Customizer.withDefaults()) // default http basic authenticated
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login/**").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll(); // vamos permitir que qualquer pessoal consiga criar um usuario
                    authorize.requestMatchers("/autores/**").hasRole("ADMIN");
                    authorize.requestMatchers("/livros/**").hasAnyRole("USER", "ADMIN");
                    // Trabalhando com authority - uma role pode ter várias authoritys
                    // authorize.requestMatchers(HttpMethod.DELETE, "/autores/**").hasAuthority("DELETAR_AUTOR");

                    authorize.anyRequest().authenticated(); // toda solicitação precisa ser autenticada
                }).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // o spring necessida de uma forma de codificar e decodificar a senha. - nesse caso vamos usar a implementação BCrypt
        // Nessa implementação não é possível decodificar a senha
        // como ele é um bean o spring vai ficar responsável por gerenciar ele
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public UserDetailsService userDetailsService(UsuarioService usuarioService) {
        // custom user details é onde o spring vai buscar os usuário para validar e autorizar
        return new CustomUserDetailsService(usuarioService);
    }
}
