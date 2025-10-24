package security.core.w4.r2.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ApiClientService {

    private final ApiClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ApiClientService(ApiClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ApiClient createClient(String name) {
        String clientId = "client_" + UUID.randomUUID().toString().substring(0, 8);
        String apiKey = "sk_" + UUID.randomUUID().toString().replace("-", "");

        ApiClient client = new ApiClient();
        client.setClientId(clientId);
        client.setApiKey(passwordEncoder.encode(apiKey));
        client.setName(name);

        ApiClient saved = clientRepository.save(client);

        // Log raw API key (chỉ hiển thị 1 lần khi tạo)
        System.out.println("=== NEW CLIENT CREATED ===");
        System.out.println("Client ID: " + clientId);
        System.out.println("API Key: " + apiKey);
        System.out.println("Save this key, it won't be shown again!");

        return saved;
    }

    public Optional<ApiClient> validateApiKey(String clientId, String apiKey) {
        return clientRepository.findByClientId(clientId)
                .filter(ApiClient::isActive)
                .filter(client -> passwordEncoder.matches(apiKey, client.getApiKey()));
    }

    public void deactivateClient(String clientId) {
        clientRepository.findByClientId(clientId)
                .ifPresent(client -> {
                    client.setActive(false);
                    clientRepository.save(client);
                });
    }
}
