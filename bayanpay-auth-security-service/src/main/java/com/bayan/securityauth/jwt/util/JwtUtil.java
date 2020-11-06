package com.bayan.securityauth.jwt.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.bayan.securityauth.model.AuthenticationResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	private String SECRET_KEY = "bayanpaykey";

	public String extractUsername(String token) {
		String extractClaim = extractClaim(token,  c-> c.get("username").toString());
		return extractClaim;
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public AuthenticationResponse generateToken(String userName, String password, String clientId, String clientSecret,
			String grantType, String scope) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("username", userName);
		claims.put("password", password);
		claims.put("clientId", clientId);
		claims.put("clientSecret", clientSecret);
		claims.put("grantType", grantType);
		claims.put("scope", scope);

		return createToken(claims);
	}

	private AuthenticationResponse createToken(Map<String, Object> claims) {
		System.out.println(claims.toString());
		Date endDate =null;
		Date currentdate=null;
		//long diffSeconds=0l;
		try {
			SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate = String.valueOf(myFormat.format(new Date()));
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, 1);
			java.util.Date dt = cal.getTime();
			String endDateThirty = String.valueOf(myFormat.format(dt));

			currentdate= myFormat.parse(currentDate);
			endDate = myFormat.parse(endDateThirty);

			//long diff = endDate.getTime() - currentdate.getTime();
			//diffSeconds = TimeUnit.MILLISECONDS.convert(diff, TimeUnit.MILLISECONDS) / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		AuthenticationResponse authenticationResponse;
		String token = Jwts.builder().setClaims(claims)
				.setExpiration(endDate)
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		System.out.println(token);
		// Date expIn = Date.from(LocalDate.now().plusDays( 31
		// ).atStartOfDay(ZoneId.systemDefault()).toInstant());
		//Date expIn = Date.from(LocalDate.now().plusDays(31).atStartOfDay(ZoneId.systemDefault()).toInstant());
		//float days = (diffSeconds / (1000*60*60*24));
		int expInDate = (int) (endDate.getTime()/1000);
		int curr = (int) (currentdate.getTime()/1000);
		System.out.println(expInDate);
		System.out.println(curr);
		
		authenticationResponse = new AuthenticationResponse(token, "Bearer", expInDate,curr,
				claims.get("scope").toString());
		return authenticationResponse;
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}