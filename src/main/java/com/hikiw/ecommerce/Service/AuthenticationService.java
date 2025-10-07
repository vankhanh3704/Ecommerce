package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Model.Request.AuthenticationRequest;
import com.hikiw.ecommerce.Model.Response.AuthenticationResponse;
import com.hikiw.ecommerce.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimNames;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    @NonFinal
    @Value("MAOAaDeZDPe7abJfyyYmg6G4TQXTdQHNrGRFTdOr1whp6zdheS/COfx2GlDSMbXH")
    protected String SIGNER_KEY;


    UserRepository userRepository;


    // service kiểm tra đăng nhập đc không
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // kiểm tra xem đầu vào đăng nhập có khớp với db không, nghĩa là tài khoản tồn tại
        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        // sau đó mình sẽ generate cái token từ tài khoản đăng nhập
        // do token nó trả ra là 1 string
        // và để tạo ra 1 token ta dùng thư viện của nimbus : nó sẽ yêu cầu 2 params : header và payload

        // tạo header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // tạo payload : 1 sẽ cần claim tương đương với body
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername()) // chủ thể (subject) của token, tức là username của người đăng nhập.
                .issuer("hiki.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(3600, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user)
                .build();

        // sau đó chuyển JWTClaimsSet thành JSON rồi đóng gói lại thành payload để kí
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // tạo đối tượng JSW bằng cách gộp header và payload
        JWSObject jwsObject = new JWSObject(header, payload);

        // sau đó phải ký token
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            // generate-random.org

        } catch (JOSEException e) {
            log.error("Cannot create Token", e);
            throw new RuntimeException(e);
        }
        var token = jwsObject.serialize();
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }
}
