package com.test.demo.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.test.demo.dto.AuthenticationRequest;
import com.test.demo.dto.IntrospectRequest;
import com.test.demo.dto.LogoutRequest;
import com.test.demo.dto.RefreshRequest;
import com.test.demo.entity.InvalidatedToken;
import com.test.demo.entity.User;
import com.test.demo.repository.InvalidatedTokenRepository;
import com.test.demo.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthenticationService {
	@Autowired
	UserRespository userRespository;
	InvalidatedTokenRepository invalidatedTokenRepository;
	protected static final String Signer_key="c0vPxkeDh+UjzV1eYstmCxmef94tlLQJDrt/ooIskStaseZaf6aDqDvSujiAXA0z";
	
	public RefreshRequest authentication(AuthenticationRequest request ) {
		RefreshRequest refreshRequest= new RefreshRequest();
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		var user= userRespository.findByUsername(request.getUsername()).orElseThrow(()-> new RuntimeException("user not found"));
		
		boolean authenticated =passwordEncoder.matches(request.getPassword(), 
				user.getPassword());
		if(!authenticated) {
			throw new RuntimeException("sai mk");
		}
		var token = generateToken(user);
		refreshRequest.setToken(token);
		return  refreshRequest;
	}
		private String generateToken (User user) {
			JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
			JWTClaimsSet jwtclaimsSet= new JWTClaimsSet.Builder()
					.subject(user.getUsername())
					.issuer("thanh.com")
					.issueTime(new Date())
					.expirationTime(new Date(
							Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()
							))
					.jwtID(UUID.randomUUID().toString())
					.claim("scope", buildScope(user))
					.build();
			Payload payload = new Payload(jwtclaimsSet.toJSONObject());
			
			JWSObject jwsObject=new JWSObject(header,payload);
			
			try{
				jwsObject.sign(new MACSigner(Signer_key.getBytes())); 
				return jwsObject.serialize();
			} catch (JOSEException e) {
				throw new RuntimeException(e);
			}
		
	}
		private String buildScope (User user) {
			StringJoiner stringJoiner= new StringJoiner(" ");
			if(	!CollectionUtils.isEmpty(user.getRoles()))
				user.getRoles().forEach(s -> stringJoiner.add(s));
			return stringJoiner.toString();
		}
		
		public boolean introspect(IntrospectRequest request) throws JOSEException, ParseException {
			var token= request.getToken();
			boolean isvalid = true;
			try{
				verifyToken(token);
			} catch(RuntimeException e){
				isvalid = false;
			};
			return isvalid;
			
		}

	public void logout(LogoutRequest request) throws JOSEException, ParseException {
		SignedJWT signToken = verifyToken(request.getToken());
		String jit = signToken.getJWTClaimsSet().getJWTID();
		Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

		InvalidatedToken invalidatedToken = new InvalidatedToken(jit, expiryTime);
		invalidatedTokenRepository.save(invalidatedToken);
	}
//		public String refreshToken(RefreshRequest request) throws JOSEException, ParseException {
//			var signedJWT=verifyToken(request.getToken());
//
//		}
		public SignedJWT verifyToken (String token) throws JOSEException, ParseException {
			JWSVerifier verifier = new MACVerifier(Signer_key.getBytes());
			SignedJWT signedJWT= SignedJWT.parse(token);
			Date expiryTime= signedJWT.getJWTClaimsSet().getExpirationTime();
			var verified =signedJWT.verify(verifier);
			if(!verified || !expiryTime.after(new Date() )) {
				throw new RuntimeException("loi");
			}
			return signedJWT;
		}
}
