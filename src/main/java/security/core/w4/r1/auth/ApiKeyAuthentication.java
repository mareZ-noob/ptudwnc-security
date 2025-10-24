package security.core.w4.r1.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Objects;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {

    private final String clientId;

    public ApiKeyAuthentication(String clientId) {
        super(Collections.singletonList(new SimpleGrantedAuthority("ROLE_API_CLIENT")));
        this.clientId = clientId;
        setAuthenticated(true);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ApiKeyAuthentication that = (ApiKeyAuthentication) o;
        return Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientId);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return clientId;
    }
}
