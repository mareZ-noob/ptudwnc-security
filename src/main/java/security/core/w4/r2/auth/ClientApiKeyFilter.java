package security.core.w4.r2.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class ClientApiKeyFilter extends OncePerRequestFilter {

    private static final String CLIENT_ID_HEADER = "X-Client-ID";
    private static final String API_KEY_HEADER = "X-API-Key";
    @Value("${api.security.internal-key}")
    private String adminApiKey;

    private final ApiClientService clientService;

    private final ApiRateLimiter rateLimiter;

    public ClientApiKeyFilter(ApiClientService clientService, ApiRateLimiter rateLimiter) {
        this.clientService = clientService;
        this.rateLimiter = rateLimiter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/public/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().startsWith("/admin/")) {
            String apiKey = request.getHeader(API_KEY_HEADER);

            if (apiKey == null || !apiKey.equals(adminApiKey)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"error\": \"Invalid or missing API key\"}"
                );
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        String clientId = request.getHeader(CLIENT_ID_HEADER);
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (clientId == null || apiKey == null) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing client credentials");
            return;
        }

        Optional<ApiClient> clientOpt = clientService.validateApiKey(clientId, apiKey);

        if (clientOpt.isEmpty()) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid client credentials");
            return;
        }

        ApiClient client = clientOpt.get();

        // Rate limiting
        if (!rateLimiter.tryConsume(clientId, client.getRequestsPerMinute())) {
            sendError(response, HttpServletResponse.SC_CONFLICT,
                    "Rate limit exceeded");
            return;
        }

        // Set authentication
        ClientAuthentication auth = new ClientAuthentication(client);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(
                String.format("{\"error\": \"%s\"}", message)
        );
    }
}
