package top.frankxxj.codeai.midware.security.rsa;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(RsaKeyProperties.PREFIX)
public record RsaKeyProperties(RSAPublicKey publicKey,
                               RSAPrivateKey privateKey) {
    public static final String PREFIX = "rsa";
}
