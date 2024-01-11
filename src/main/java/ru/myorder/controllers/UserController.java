package ru.myorder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.myorder.config.jwt.JwtUtils;
import ru.myorder.dtos.JwtDTO;
import ru.myorder.dtos.MessageDTO;
import ru.myorder.models.RefreshToken;
import ru.myorder.models.User;
import ru.myorder.payloads.SignInRequest;
import ru.myorder.payloads.SignUpRequest;
import ru.myorder.repositories.UserRepository;
import ru.myorder.services.RefreshTokenService;
import ru.myorder.services.UserDetailsImpl;
import ru.myorder.services.UserService;

@Tag(name="UserController")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Operation(summary="вход в аккаунт")
    @PostMapping("/signin")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = JwtDTO.class))})
    public ResponseEntity<?> authenticateUser(@RequestBody SignInRequest signInRequest) {
        LOGGER.info("SIGNIN");
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl accountDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(accountDetails);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(accountDetails.getId());
        return ResponseEntity.ok(new JwtDTO(jwt, refreshToken.getToken()));
    }

    @Operation(summary="регистрация аккаунта")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = MessageDTO.class))})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        LOGGER.info("SIGN UP");
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageDTO("Пользователь с таким именем уже существует"));
        }
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), signUpRequest.getIsAdmin());
        userRepository.save(user);
        return ResponseEntity.ok(new MessageDTO("Пользователь зарегистирован!"));
    }
}
