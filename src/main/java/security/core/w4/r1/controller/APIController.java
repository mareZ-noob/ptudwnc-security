// package security.core.w4.r1.controller;
//
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetails;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import java.util.Map;
//
// @RestController
// @RequestMapping("/api")
// public class APIController {
//
//     // curl -H "X-API-Key: internal-simple-api-key" \
//     //      http://localhost:8088/api/data
//     @GetMapping("/data")
//     public ResponseEntity<?> getData() {
//         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//         String clientId = auth.getName();
//
//         String clientIp = "N/A";
//         Object details = auth.getDetails();
//
//         if (details instanceof WebAuthenticationDetails) {
//             clientIp = ((WebAuthenticationDetails) details).getRemoteAddress();
//         }
//
//         return ResponseEntity.ok(Map.of(
//                 "message", "Protected data",
//                 "client", clientId,
//                 "clientIp", clientIp,
//                 "timestamp", System.currentTimeMillis()
//         ));
//     }
//
//     // curl http://localhost:8088/api/public/health
//     @GetMapping("/public/health")
//     public ResponseEntity<?> health() {
//         return ResponseEntity.ok(Map.of("status", "UP"));
//     }
//
// }
