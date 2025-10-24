package security.core.w4.r2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import security.core.w4.r2.auth.ApiClient;
import security.core.w4.r2.auth.ApiClientService;

import java.util.Map;

@RestController
@RequestMapping("/admin/clients")
public class AdminClientController {
    private final ApiClientService clientService;

    public AdminClientController(ApiClientService clientService) {
        this.clientService = clientService;
    }

    // curl -X POST http://localhost:8088/admin/clients \
    //   -H "Content-Type: application/json" -H "X-API-Key: internal-simple-api-key" \
    //   -d '{"name": "Mobile App", "requestsPerMinute": 60}'
    @PostMapping
    public ResponseEntity<?> createClient(@RequestBody CreateClientRequest request) {
        ApiClient client = clientService.createClient(
            request.getName(),
            request.getRequestsPerMinute()
        );

        return ResponseEntity.ok(Map.of(
            "clientId", client.getClientId(),
            "name", client.getName(),
            "message", "Check server logs for API key"
        ));
    }

    @DeleteMapping("/{clientId}")
    public ResponseEntity<?> deactivateClient(@PathVariable String clientId) {
        clientService.deactivateClient(clientId);
        return ResponseEntity.ok(Map.of("message", "Client deactivated"));
    }
}
