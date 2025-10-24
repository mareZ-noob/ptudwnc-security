package security.core.w4.r2.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClientRequest {
    private String name;
    private Integer requestsPerMinute = 100;
}
