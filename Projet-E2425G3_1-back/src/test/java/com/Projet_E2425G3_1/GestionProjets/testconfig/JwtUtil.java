package com.Projet_E2425G3_1.GestionProjets.testconfig;

import com.Projet_E2425G3_1.GestionProjets.config.JwtUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String testEmail;
    private String accessToken;
    private String refreshToken;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        testEmail = "test@example.com";
        accessToken = jwtUtil.generateAccessToken(testEmail);
        refreshToken = jwtUtil.generateRefreshToken(testEmail);
    }

    @Test
    void testGenerateAccessToken() {
        assertNotNull(accessToken);
        String email = jwtUtil.extractEmail(accessToken, false);
        assertEquals(testEmail, email);
    }

    @Test
    void testGenerateRefreshToken() {
        assertNotNull(refreshToken);
        String email = jwtUtil.extractEmail(refreshToken, true);
        assertEquals(testEmail, email);
    }

    @Test
    void testExtractEmailFromAccessToken() {
        String extractedEmail = jwtUtil.extractEmail(accessToken, false);
        assertEquals(testEmail, extractedEmail);
    }

    @Test
    void testExtractEmailFromRefreshToken() {
        String extractedEmail = jwtUtil.extractEmail(refreshToken, true);
        assertEquals(testEmail, extractedEmail);
    }

    @Test
    void testValidateAccessToken() {
        assertTrue(jwtUtil.validateToken(accessToken, false));
    }

    @Test
    void testValidateRefreshToken() {
        assertTrue(jwtUtil.validateToken(refreshToken, true));
    }

    @Test
    void testExpiredAccessToken() throws InterruptedException {
        // Simulate token expiration (by waiting or generating a token with short expiration)
        String expiredToken = Jwts.builder()
                .setSubject(testEmail)
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 31))
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60 * 30))
                .signWith(jwtUtil.getSecretKey(false), SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtUtil.validateToken(expiredToken, false));
    }


    @Test
    void testInvalidSignature() {
        String tamperedToken = accessToken.substring(0, accessToken.length() - 5) + "abcd";
        assertFalse(jwtUtil.validateToken(tamperedToken, false));
    }
}
