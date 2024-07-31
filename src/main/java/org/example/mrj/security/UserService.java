package org.example.mrj.security;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService tokenService;

    public ResponseEntity<ApiResponse<Token>> login(String username, String password)
    {
        ApiResponse<Token> response = new ApiResponse<>();
        Optional<User> optional = userRepository.findByUsername(username);
        if (optional.isEmpty())
        {
            response.setMessage("Username or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        User user = optional.get();
        if (passwordEncoder.matches(password, user.getPassword()))
        {
            Token token = new Token(tokenService.generateToken(username));
            response.setMessage(String.format(
                    "Successfully logged in as: %s.", user.getUsername()));
            response.setData(token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else
        {
            response.setMessage("Username or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
