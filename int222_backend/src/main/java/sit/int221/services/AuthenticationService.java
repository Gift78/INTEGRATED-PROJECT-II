package sit.int221.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sit.int221.dtos.AuthenticationRequestDTO;
import sit.int221.dtos.AuthenticationResponseDTO;
import sit.int221.entities.User;
import sit.int221.exceptions.UnauthorizedException;
import sit.int221.exceptions.UserNotFoundException;
import sit.int221.repositories.UserRepository;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
        try {
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserNotFoundException(request.getUsername()));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return new AuthenticationResponseDTO(token, refreshToken);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Password does not match for username: " + request.getUsername());
        }
    }
}
