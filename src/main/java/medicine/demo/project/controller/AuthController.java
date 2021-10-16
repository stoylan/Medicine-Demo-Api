package medicine.demo.project.controller;


import medicine.demo.project.core.utilities.exceptions.TokenRefreshException;
import medicine.demo.project.core.utilities.results.DataResult;
import medicine.demo.project.dto.AuthToken;
import medicine.demo.project.dto.Response.TokenRefreshResponse;
import medicine.demo.project.dto.Response.UserRegisterResponse;
import medicine.demo.project.dto.TokenRefreshDto;
import medicine.demo.project.dto.UserDto;
import medicine.demo.project.entity.JwtToken;
import medicine.demo.project.entity.LoginRequest;
import medicine.demo.project.security.jwt.RefreshTokenServiceJWT;
import medicine.demo.project.security.jwt.TokenProvider;
import medicine.demo.project.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@AllArgsConstructor
public class AuthController {
    private final TokenProvider tokenService;

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final RefreshTokenServiceJWT refreshTokenJWT;

    private final UserRegisterResponse userRegisterResponse;

    @PostMapping("/login")
    public ResponseEntity<AuthToken> login(@RequestBody LoginRequest loginRequest) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenService.generateToken(authentication);
        String refreshToken = refreshTokenJWT.createRefreshToken(refreshTokenJWT.getIdByUsername(authentication.getName())).getRefreshToken();
        return ResponseEntity.ok(new AuthToken(token,refreshToken));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<TokenRefreshResponse> refreshtoken(@RequestBody TokenRefreshDto tokenRefreshDto) {
        String requestRefreshToken = tokenRefreshDto.getRefreshToken();
        return refreshTokenJWT.findByToken(requestRefreshToken)
                .map(refreshTokenJWT::verifyExpiration)
                .map(JwtToken::getUser)
                .map(user -> {
                    String token = tokenService.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token veri tabanında bulunamadı."));
    }


    @PostMapping("/register")
    public ResponseEntity<DataResult<UserRegisterResponse>> saveUser(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.save(userDto));
    }

}