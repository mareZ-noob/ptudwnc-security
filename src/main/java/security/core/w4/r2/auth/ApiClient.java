package security.core.w4.r2.auth;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_clients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String apiKey; // Hashed

    @Column(nullable = false)
    private String name;

    private boolean active = true;

    private Integer requestsPerMinute = 100;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
