package medicine.demo.project.integrationTest;

import medicine.demo.project.core.utilities.exceptions.JwtTokenException;
import medicine.demo.project.core.utilities.exceptions.TokenRefreshException;
import medicine.demo.project.dto.TokenRefreshDto;
import medicine.demo.project.entity.JwtToken;
import medicine.demo.project.security.jwt.RefreshTokenServiceJWT;
import medicine.demo.project.security.jwt.TokenProvider;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RefreshTokenTest extends AbstractJwtTest<TokenRefreshDto> {

    @Autowired
    private RefreshTokenServiceJWT refreshTokenJWT;

    @Autowired
    MockMvc mvc;

    @Autowired
    TokenProvider tokenProvider;

    String accessToken;

    String getRefreshToken;

    String tokenType;

    JwtToken refreshToken;

    @BeforeEach
    void setupJwt() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(null);
        refreshToken = refreshTokenJWT.createRefreshToken(1L);

        TokenRefreshDto tokenRefreshDto = new TokenRefreshDto();
        tokenRefreshDto.setRefreshToken(refreshToken.getRefreshToken());
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/api/auth/refreshtoken")
                        .content(objectMapper.writeValueAsString(tokenRefreshDto))
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        accessToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.accessToken");
        getRefreshToken = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.refreshToken");
        tokenType = JsonPath.read(mvcResult.getResponse().getContentAsString(), "$.tokenType");

    }

    @Test
    void create_RefreshToken_And_Get_ValidateJwt() throws Exception {

        assertEquals("Bearer", tokenType);
        assertEquals(getRefreshToken, refreshToken.getRefreshToken());
        assertTrue(tokenProvider.validateJwtToken(accessToken));
    }

    @Test
    void setUnValidToken_then_get_jwtTokenException() {
        String unexceptedToken = "unexceptedToken";
        JwtTokenException jwtTokenException = assertThrows(JwtTokenException.class, () -> tokenProvider.validateJwtToken(unexceptedToken));
        assertEquals("Geçersiz JWT tokeni = " + unexceptedToken, jwtTokenException.getMessage());

    }

    @Test
    void setOutTimeRefreshToken_then_getTokenRefreshException() {
        refreshToken.setExpiryDate(Instant.now());
        TokenRefreshException tokenRefreshException = assertThrows(TokenRefreshException.class, () -> refreshTokenJWT.verifyExpiration(refreshToken));
        assertEquals("Refresh token : [" + refreshToken.getRefreshToken() + "]: Refresh tokenin süresi dolmuştur. Lütfen tekrar giriş yapınız.", tokenRefreshException.getMessage());
    }
}
