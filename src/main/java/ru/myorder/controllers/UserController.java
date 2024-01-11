package ru.myorder.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.java.Log;
import org.apache.coyote.Response;
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
import ru.myorder.dtos.*;
import ru.myorder.exceptions.AppException;
import ru.myorder.exceptions.TokenRefreshException;
import ru.myorder.exceptions.UserExistsException;
import ru.myorder.models.RefreshToken;
import ru.myorder.models.User;
import ru.myorder.payloads.SignInRequest;
import ru.myorder.payloads.SignUpRequest;
import ru.myorder.payloads.TokenRefreshRequest;
import ru.myorder.payloads.UserEditRequest;
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


        RefreshToken refreshToken = refreshTokenService.createRefreshTokenOrUpdate(accountDetails.getId());
        return ResponseEntity.ok(new JwtDTO(jwt, refreshToken.getToken()));
    }

    @Operation(summary="регистрация аккаунта")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = AppException.class))})
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        LOGGER.info("SIGN UP");
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new AppException(HttpStatus.FORBIDDEN.value(), "пользователь с таким именем уже существует"), HttpStatus.FORBIDDEN);
        }
        User user = new User(signUpRequest.getUsername(), encoder.encode(signUpRequest.getPassword()), false);
        userRepository.save(user);
        return new ResponseEntity<>(new AppException(HttpStatus.OK.value(), "регистрация прошла успешно"), HttpStatus.OK);
    }

    @Operation(summary="получение refresh token")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = TokenRefreshDTO.class))})
    @ApiResponse(responseCode = "401", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = ErrorMessageDTO.class))})
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(account -> {
                    String token = jwtUtils.generateTokenFromUsername(account.getUsername(), account.getIsAdmin(), account.getId());
                    return ResponseEntity.ok(new TokenRefreshDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token не найден"));
    }

    @Operation(summary = "получение данных о текущем пользователе")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = UserDTO.class))})
    @ApiResponse(responseCode = "401", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = AppException.class))})
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentAccountInfo(){
        LOGGER.info("GET CURRENT ACCOUNT INFO");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        LOGGER.info(userDetails.getIsAdmin().toString());
        User user = userRepository.findByUsername(username).get();
        if(user != null){
            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getIsAdmin());
            return ResponseEntity.ok(userDTO);
        }

        return new ResponseEntity<>(new AppException(HttpStatus.NOT_FOUND.value(), "данные не найдены"), HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "редактирование данных пользователя")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = AppException.class))})
    @ApiResponse(responseCode = "401", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = AppException.class))})
    @PutMapping("/edit/{userId}")
    public ResponseEntity<AppException> editUserById(@PathVariable("userId") Long userId, @RequestBody UserEditRequest userEditRequest){
        LOGGER.info("EDIT USER BY ID");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getIsAdmin() || userDetails.getId() == userId){
            try{
                LOGGER.info(userEditRequest.getUsername());
                userEditRequest.setPassword(encoder.encode(userEditRequest.getPassword()));
                if(userService.updateUserData(userId, userEditRequest)){
                    return new ResponseEntity<AppException>(new AppException(HttpStatus.OK.value(), "данные пользователя изменены"), HttpStatus.OK);
                }
            }
            catch (UserExistsException ue){
                return new ResponseEntity<AppException>(new AppException(HttpStatus.FORBIDDEN.value(), ue.getMessage()), HttpStatus.FORBIDDEN);
            }
            catch (RuntimeException re){
                return new ResponseEntity<AppException>(new AppException(HttpStatus.FORBIDDEN.value(), "что-то пошло не так"), HttpStatus.FORBIDDEN);
            }

        }
        return new ResponseEntity<AppException>(new AppException(HttpStatus.FORBIDDEN.value(), "не хватает прав для изменения данных"), HttpStatus.FORBIDDEN);
    }

    @Operation(summary = "удаление пользователя")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = AppException.class))})
    @ApiResponse(responseCode = "401", content = {@Content(mediaType = "application/json", schema=@Schema(implementation = AppException.class))})
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<AppException> deleteUserById(@PathVariable("userId") Long userId){
        LOGGER.info("DELETE USER BY ID");
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userDetails.getIsAdmin()){
            refreshTokenService.deleteByUserId(userId);
            userRepository.deleteById(userId);
            return  ResponseEntity.ok(new AppException(HttpStatus.OK.value(), "пользователь с именем"));
        }
        return  new ResponseEntity<AppException>(new AppException(HttpStatus.FORBIDDEN.value(), "не хватает прав для удаления"), HttpStatus.FORBIDDEN);

    }

}
