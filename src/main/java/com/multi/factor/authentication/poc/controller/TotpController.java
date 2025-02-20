package com.multi.factor.authentication.poc.controller;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/totp")
public class TotpController {

    private final GoogleAuthenticator gAuth;

    public TotpController() {
        this.gAuth = new GoogleAuthenticator();
    }

    // Endpoint to generate a secret key and QR code URL
    @GetMapping("/generate")
    public String generate(@RequestParam String username) {
        // Generate a secret key
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        // Generate a QR code URL
        String qrCodeUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(
                "MyApp", // Issuer
                username, // Account name (e.g., user email)
                key
        );

        return "Secret Key: " + key.getKey() + "\nQR Code URL: " + qrCodeUrl;
    }

    // Endpoint to verify a TOTP code
    @PostMapping("/verify")
    public String verify(@RequestParam String secretKey, @RequestParam int totpCode) {
        boolean isValid = gAuth.authorize(secretKey, totpCode);
        return isValid ? "TOTP code is valid!" : "Invalid TOTP code.";
    }
}