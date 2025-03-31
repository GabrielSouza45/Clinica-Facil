package br.app.tads.clinica_facil.infra.security;

import br.app.tads.clinica_facil.repository.AdminRepository;
import br.app.tads.clinica_facil.repository.DoctorRepository;
import br.app.tads.clinica_facil.repository.PatientRepository;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthorizationService authorizationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Gson gson = new Gson();
        String tokenExpired = gson.toJson(new ErrorMessage("Token Expirado."));
        String tokenInvalid = gson.toJson(new ErrorMessage("Token Inválido."));

        try{
            var token = this.recoverToken(request);
            if (token != null){
                var login = tokenService.validateToken(token);
                System.out.println("LOGIN AUTH TOKEN -> " + login);
                System.out.println("AUTH TOKEN -> " + token);
                UserDetails user = authorizationService.loadUserByUsername(login);

                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
                filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(tokenExpired);
            response.getWriter().flush();
            return;
        } catch (JWTDecodeException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(tokenInvalid);
            response.getWriter().flush();
            return;
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        System.out.println(request);
        System.out.println(authHeader);
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private static class ErrorMessage{
        private String message;
        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
