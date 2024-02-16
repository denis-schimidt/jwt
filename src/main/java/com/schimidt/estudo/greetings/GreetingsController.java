package com.schimidt.estudo.greetings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/greetings", produces = MediaType.TEXT_HTML_VALUE)
class GreetingsController {

    private final TimeBasedGreetingGenerator timeBasedGreetingGenerator;

    public GreetingsController(TimeBasedGreetingGenerator timeBasedGreetingGenerator) {
        this.timeBasedGreetingGenerator = timeBasedGreetingGenerator;
    }

    @GetMapping(path = "/without-authentications")
    ResponseEntity<String> generateGenericGreeting() {
        String message = """
                    <h2>
                        %s, usuário (não atenticado)
                    </h2>
                """.formatted(timeBasedGreetingGenerator.generateGreetingBasedOnCurrentTime());

        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/using-cookies")
    ResponseEntity<String> generateGreetingBasedOn(@AuthenticationPrincipal OidcUser principal,
                                                   @CookieValue(value = "JSESSIONID", defaultValue = "No JSESSIONID found") String jSessionID) {
        String message = """
                    <h2>
                        %s, %s você está autenticado com as seguintes informações:
                        <ul>
                            <li>JSESSIONID=%s</li>
                            <li>JWT Token: %s</li>
                        </ul>
                    </h2>
                """.formatted(timeBasedGreetingGenerator.generateGreetingBasedOnCurrentTime(),
                principal.getEmail(),
                jSessionID,
                principal.getIdToken().getTokenValue());

        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/using-jwt-tokens")
    public ResponseEntity<String> generateGreetingBasedOn(@AuthenticationPrincipal Jwt jwt) {
        String message = """
                    <h2>
                        %s, %s você está autenticado com as seguintes informações:
                        <ul>
                            <li>JWT Token: %s</li>
                            <li>Claims: %s,</li>
                            <li>Headers: %s</li>
                        </ul>
                    </h2>
                """.formatted(timeBasedGreetingGenerator.generateGreetingBasedOnCurrentTime(),
                jwt.getClaims().get("email"),
                jwt.getTokenValue(),
                jwt.getClaims(),
                jwt.getHeaders());

        return ResponseEntity.ok(message);
    }
}
