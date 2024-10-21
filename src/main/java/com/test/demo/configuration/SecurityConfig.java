package com.test.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	private final String[] public_endpoint= {"/users","/token","/Introspect","/logout","/Logout"};
 	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(request ->
		request.requestMatchers(HttpMethod.POST, public_endpoint).permitAll()
		//.requestMatchers(HttpMethod.GET,"/users")
		//.hasAnyAuthority("ROLE_ADMIN")
		.anyRequest().authenticated());
		
		httpSecurity.oauth2ResourceServer(oauth2 ->
			oauth2.jwt(JwtConfigurer -> JwtConfigurer.decoder(jwtDecoder())
					.jwtAuthenticationConverter(jwtAuthenticationConverter()))
		);
		httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
		return httpSecurity.build();
	}
 	@Bean
 	JwtDecoder jwtDecoder() {
 		SecretKeySpec secretKeySpec = new SecretKeySpec("c0vPxkeDh+UjzV1eYstmCxmef94tlLQJDrt/ooIskStaseZaf6aDqDvSujiAXA0z".getBytes(), "HS512");
 		return NimbusJwtDecoder
 				.withSecretKey(secretKeySpec)
 				.macAlgorithm(MacAlgorithm.HS512)
 				.build() 
 				;
 	}

	@Bean
 	JwtAuthenticationConverter jwtAuthenticationConverter() {
 		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter=new JwtGrantedAuthoritiesConverter();
 		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
 		JwtAuthenticationConverter jwtAuthenticationConverter= new JwtAuthenticationConverter();
 		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
 		return jwtAuthenticationConverter;
	 }
	@Bean
	public CorsFilter corsFilter(){
		 CorsConfiguration corsConfiguration = new CorsConfiguration();
		 corsConfiguration.addAllowedOrigin("*");
		 corsConfiguration.addAllowedMethod("*");
		 corsConfiguration.addAllowedHeader("*");
		 UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		 urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
		 return new CorsFilter(urlBasedCorsConfigurationSource);
	}
}