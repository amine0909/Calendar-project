package com.businessanddecisions.controllers;

import com.businessanddecisions.models.AuthToken;
import com.businessanddecisions.models.LoginUser;
import com.businessanddecisions.models.UserModel;
import com.businessanddecisions.services.UserService;
import config.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

import static config.Constants.ACCESS_TOKEN_VALIDITY_SECONDS;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/token")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/generate-token")
    public ResponseEntity register(@RequestBody LoginUser loginUser) throws AuthenticationException {
        System.out.println(loginUser);
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserModel user = userService.findOne(loginUser.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        final long expireIn = ACCESS_TOKEN_VALIDITY_SECONDS * 1000;

        ZoneId z = ZoneId.of("Africa/Tunis");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        df.setTimeZone(TimeZone.getTimeZone(z));
        Calendar c = df.getCalendar();
        c.setTime(new Date());

        c.add(Calendar.SECOND,Integer.parseInt(String.valueOf(expireIn)));
        Date d = c.getTime();
        String finalDateFormatToSave= df.format(d);

        return ResponseEntity.ok(new AuthToken(token,finalDateFormatToSave));
    }

}
