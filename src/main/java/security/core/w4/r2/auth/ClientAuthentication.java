package security.core.w4.r2.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class ClientAuthentication extends AbstractAuthenticationToken {

    private final ApiClient principal;

    public ClientAuthentication(ApiClient principal) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_CLIENT")));
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
