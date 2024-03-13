package org.acme.auth;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class JwtUtils {

    private static final String PRIVATE_KEY_LOCATION = "../jwt/privateKey.pem"; // Path to your private key file

    public static String generateJwtToken (String username) throws IOException {
        Set<String> groups = new HashSet<>(Arrays.asList("user"));

        String privateKeyContent = new String(Files.readAllBytes(Paths.get(PRIVATE_KEY_LOCATION)));
        PrivateKey privateKey = JwtUtils.loadPrivateKey(privateKeyContent);

        return Jwt.issuer("jwt-token")
                .subject("course")
                .groups(groups)
                .expiresIn(3600) // Token expiration time in seconds
                .sign(privateKey);
    }

    public static PrivateKey loadPrivateKey(String privateKeyContent) {
        try {
            // Remove any existing "BEGIN PRIVATE KEY" and "END PRIVATE KEY" tags
            privateKeyContent = privateKeyContent.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");

            // Decode the base64-encoded private key
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyContent);

            // Create a PKCS8EncodedKeySpec from the decoded bytes
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

            // Get the RSA KeyFactory instance
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            // Generate the private key
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace(); // Handle exception properly in your application
            return null;
        }
    }
}