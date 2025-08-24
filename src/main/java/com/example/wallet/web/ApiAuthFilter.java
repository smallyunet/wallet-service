package com.example.wallet.web;

import com.example.wallet.config.AuthProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class ApiAuthFilter extends OncePerRequestFilter {
    private final AuthProperties authProperties;

    @Autowired
    public ApiAuthFilter(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
      // Allow health check endpoint
      String path = request.getRequestURI();
    if (path.contains("/health")
      || path.startsWith("/swagger-ui")
      || path.startsWith("/v3/api-docs")
      || path.startsWith("/swagger-resources")
      || path.equals("/swagger-ui.html")) {
      filterChain.doFilter(request, response);
      return;
      }
      // Skip authentication if disabled
      if (!authProperties.isEnabled()) {
        filterChain.doFilter(request, response);
        return;
      }
      // Validate request headers
      String clientType = request.getHeader("X-Client-Type");
      String clientKey = request.getHeader("X-Client-Key");
      boolean ok = false;
      
      if ("android".equalsIgnoreCase(clientType)) {
        ok = authProperties.getAndroidKey() != null && authProperties.getAndroidKey().equals(clientKey);
      } else if ("ios".equalsIgnoreCase(clientType)) {
        ok = authProperties.getIosKey() != null && authProperties.getIosKey().equals(clientKey);
      } else {
        // Allow other client types if the key matches any valid key
        ok = (authProperties.getAndroidKey() != null && authProperties.getAndroidKey().equals(clientKey)) || 
           (authProperties.getIosKey() != null && authProperties.getIosKey().equals(clientKey));
      }
      
      if (!ok) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Unauthorized\"}");
        return;
      }
      filterChain.doFilter(request, response);
    }
}
