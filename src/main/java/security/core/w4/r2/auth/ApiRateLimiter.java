package security.core.w4.r2.auth;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ApiRateLimiter {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String clientId, int requestsPerMinute) {
        return buckets.computeIfAbsent(clientId, key ->
                Bucket.builder()
                        .addLimit(limit -> limit
                                .capacity(requestsPerMinute)
                                .refillGreedy(requestsPerMinute, Duration.ofMinutes(1))
                        )
                        .build()
        );
    }

    public boolean tryConsume(String clientId, int requestsPerMinute) {
        Bucket bucket = resolveBucket(clientId, requestsPerMinute);
        return bucket.tryConsume(1);
    }
}