package security.core.w4.r2.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class ClientAuthentication extends AbstractAuthenticationToken {

    private final ApiClient client;

    public ClientAuthentication(ApiClient client) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_CLIENT")));
        this.client = client;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return client;
    }

    public ApiClient getClient() {
        return client;
    }
}
