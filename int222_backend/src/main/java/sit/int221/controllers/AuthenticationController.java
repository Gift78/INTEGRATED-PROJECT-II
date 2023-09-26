package sit.int221.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import sit.int221.dtos.AuthenticationRequestDTO;
import sit.int221.dtos.AuthenticationResponseDTO;
import sit.int221.services.AuthenticationService;

@RestController
@RequestMapping("/api/token")
@CrossOrigin(origins= {"http://localhost:5173", "https://intproj22.sit.kmutt.ac.th"})
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public AuthenticationResponseDTO authenticate(@RequestBody AuthenticationRequestDTO request) {
        return authenticationService.authenticate(request);
    }

    @GetMapping
    public AuthenticationResponseDTO refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        return authenticationService.refreshToken(authorizationHeader);
    }
}