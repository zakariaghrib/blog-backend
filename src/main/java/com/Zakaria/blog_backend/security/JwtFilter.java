package com.Zakaria.blog_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    // L'annotation @Lazy permet d'éviter un problème de "boucle infinie" (Circular Dependency)
    // lors du démarrage de Spring Security
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. On regarde s'il y a un paramètre "Authorization" dans la requête
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Le standard veut que le token ressemble à ça : "Bearer eyJhbGciOiJIUzI1NiJ9..."
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // On enlève les 7 premières lettres ("Bearer ") pour ne garder que le token
            username = jwtUtil.extractUsername(jwt); // On demande à notre usine de décrypter le nom d'utilisateur
        }

        // 3. Si on a trouvé un nom d'utilisateur et qu'il n'est pas encore connecté dans ce "tour" de sécurité
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // On va chercher l'utilisateur dans la base de données
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // On demande à notre usine si le token est toujours valide pour ce gars-là
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // Si c'est bon, on crée son passeport officiel pour Spring Security
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // On lui ouvre la porte !
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // 4. On passe la requête au vigile suivant (ou au contrôleur si c'est la fin)
        filterChain.doFilter(request, response);
    }
}