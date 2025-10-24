package security.core.w4.r2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import security.core.w4.r2.auth.ApiClient;
import security.core.w4.r2.auth.ClientAuthentication;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ClientApiController {

    // curl -H "X-Client-ID: client_abc12345" \
    //      -H "X-API-Key: sk_xxxxxxxxxxxx" \
    //      http://localhost:8088/api/data
    @GetMapping("/data")
    public ResponseEntity<?> getData() {
        ClientAuthentication auth = (ClientAuthentication)
            SecurityContextHolder.getContext().getAuthentication();

        ApiClient client = auth.getClient();

        return ResponseEntity.ok(Map.of(
            "message", "Protected data",
            "clientId", client.getClientId(),
            "clientName", client.getName()
        ));
    }
}

