package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Entity.InvalidatedToken;
import com.hikiw.ecommerce.Entity.UserEntity;
import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Model.Request.AuthenticationRequest;
import com.hikiw.ecommerce.Model.Request.IntrospectRequest;
import com.hikiw.ecommerce.Model.Request.LogoutRequest;
import com.hikiw.ecommerce.Model.Request.RefreshToken;
import com.hikiw.ecommerce.Model.Response.AuthenticationResponse;
import com.hikiw.ecommerce.Model.Response.IntrospectResponse;
import com.hikiw.ecommerce.Repository.InvalidatedTokenRepository;
import com.hikiw.ecommerce.Repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
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
    InvalidatedTokenRepository invalidatedTokenRepository;


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
        var token = generateToken(user);
        // đăng nhập thành công nó sẽ trả cho mình cái token để đem nó đi authenticate
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    // hàm kiểm tra tính hợp lệ, hết hạn ... của token
    public IntrospectResponse introspectResponse(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return  IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // Dùng thư viện Nimbus JOSE + JWT để tạo đối tượng MACVerifier.
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Chuyển chuỗi JWT thành đối tượng SignedJWT. do bên trong signedJWT có header, payload, signature
        SignedJWT signedJWT = SignedJWT.parse(token);


        // kiểm tra token hết hạn hay chưa
        Date expirationTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(3600, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        // kiểm tra chữ ký signature
        var verified = signedJWT.verify(verifier);
        //token chỉ được công nhận khi vừa đúng chữ ký, vừa còn hạn.
        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    // build scope
    public String buildScope(UserEntity user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(roleEntity -> {
                stringJoiner.add("ROLE_" + roleEntity.getName());
            });
        }
        return stringJoiner.toString();
    }


    // function logout
    public void logout(LogoutRequest logoutRequest){
        try {
            // lấy token đang đăng nhập ( token còn hạn)
            var signToken = verifyToken(logoutRequest.getToken(), false);

            String jti = signToken.getJWTClaimsSet().getJWTID(); // lấy username
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime(); // lấy thời gian hết hạn của token ra


            // set lại cái invalidatedToken : là 1 bảng lưu các token hết hạn ( những token này sẽ không dùng được nx)
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(jti)
                    .expiryTime(expiryTime)
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // refresh token
    public AuthenticationResponse refreshToken(RefreshToken refreshToken) throws ParseException, JOSEException {
        var signedJWT = verifyToken(refreshToken.getToken(), false).getJWTClaimsSet();
        var jit = signedJWT.getJWTID();
        var expiryTime = signedJWT.getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .expiryTime(expiryTime)
                .id(jit)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
        var username = signedJWT.getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var token = generateToken(user);
        return AuthenticationResponse.builder().authenticated(true).token(token).build();
    }

    public String generateToken(UserEntity user){
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
                .claim("scope", buildScope(user))
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
        return jwsObject.serialize();
    }
}
