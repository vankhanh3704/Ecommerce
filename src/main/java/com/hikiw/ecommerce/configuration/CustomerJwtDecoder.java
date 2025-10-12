package com.hikiw.ecommerce.configuration;


import com.hikiw.ecommerce.Model.Request.IntrospectRequest;
import com.hikiw.ecommerce.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;


// dùng để xác thực và giải mã JWT (JSON Web Token) theo cơ chế tự kiểm tra (introspection).
@Component
@RequiredArgsConstructor
public class CustomerJwtDecoder implements JwtDecoder {
    @NonFinal
    @Value("MAOAaDeZDPe7abJfyyYmg6G4TQXTdQHNrGRFTdOr1whp6zdheS/COfx2GlDSMbXH")
    protected String SIGNER_KEY;

    @Autowired
    AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;


    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticationService.introspectResponse(
                    IntrospectRequest.builder().token(token).build());
            if (!response.isValid()) throw new JwtException("Token invalid");
        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
