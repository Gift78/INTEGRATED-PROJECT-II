package sit.int221.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.int221.dtos.AuthenticationRequestDTO;
import sit.int221.dtos.AuthenticationResponseDTO;
import sit.int221.services.AuthenticationService;

@RestController
@RequestMapping("/api/token")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public AuthenticationResponseDTO authenticate(@RequestBody AuthenticationRequestDTO request) {
        return authenticationService.authenticate(request);
    }
}
