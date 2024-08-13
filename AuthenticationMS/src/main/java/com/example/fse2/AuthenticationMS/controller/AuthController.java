package com.example.fse2.AuthenticationMS.controller;

import com.example.fse2.AuthenticationMS.models.AppUser;
import com.example.fse2.AuthenticationMS.models.LoginDTO;
import com.example.fse2.AuthenticationMS.models.UserDTO;
import com.example.fse2.AuthenticationMS.repository.UserRepository;
import com.example.fse2.AuthenticationMS.services.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController()
@RequestMapping("/company")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("UP", HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication auth){
        log.info("Fetching the profile details");
        return ResponseEntity.ok(authenticatedResponse(auth));
    }

    @GetMapping("/getRole")
    public String getRoles(@RequestHeader("Authorization") String token){
        log.info("Getting roles associated with User");
        return getRoleDetails(token);
    }

    @GetMapping("/isValidToken")
    public boolean isValidToken(@RequestHeader("Authorization") String token) throws Exception {
        log.info("Checking whether token is valid or not");
        return isValidTokenOrNot(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDTO userDTO, BindingResult result){
        log.info("Registering the user");

        if (result.hasErrors()){
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String >();

            for (int i=0;i<errorsList.size();i++){
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        try {
            HashMap<String, Object> userCreated = createUser(userDTO);
            return ResponseEntity.ok(userCreated);
        } catch (Exception e){
            System.out.println("unable to create user: "+e.getMessage());
        }
        return ResponseEntity.badRequest().body("Error");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult result){
        if (result.hasErrors()){
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String >();

            for (int i=0;i<errorsList.size();i++){
                var error = (FieldError) errorsList.get(i);
                errorsMap.put(error.getField(),error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }
        try {
            HashMap<String,Object> loginDetails = validatingLoginDetails(loginDTO);
            return ResponseEntity.ok().body(loginDetails);
        } catch (Exception e){
            System.out.println("Unable to login the user: "+e.getMessage());
        }
        return ResponseEntity.badRequest().body("Unable to login the user");
    }

    private HashMap<String,Object> authenticatedResponse(Authentication auth){
        HashMap<String,Object> response = new HashMap<>();
        AppUser user = (AppUser) auth.getPrincipal();
        response.put("Username",user.getEmail());
        response.put("Authorities",auth.getAuthorities());
        response.put("User",user);

        return response;
    }

    private String getRoleDetails(String token){
        String jwt = token.substring(7);
        Claims claims = jwtService.getTokenClaims(jwt);

        AppUser user = userRepository.findByEmail(claims.getSubject());
        return user.getRole();
    }

    private boolean isValidTokenOrNot(String token) throws Exception {
        if(token==null ){
            throw new Exception("Authorization bearer token is Null");
        }
        if (!token.startsWith("Bearer ")){
            throw new Exception("Authorization bearer word is not found");
        }

        String jwt = token.substring(7);
        Claims claims = jwtService.getTokenClaims(jwt);

        if (claims != null){
            return true;
        }else {
            return false;
        }
    }

    private HashMap<String, Object > validatingLoginDetails(LoginDTO loginDTO) {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );
            AppUser appUser = userRepository.findByEmail(loginDTO.getEmail());

            String jwtToken = jwtService.createJwtToken(appUser);
            HashMap<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("user", appUser);

            return response;
    }

    private HashMap<String ,Object> createUser(UserDTO userDTO){
        AppUser appUser = new AppUser();
        appUser.setUserName(userDTO.getUserName());
        appUser.setEmail(userDTO.getEmail());
        appUser.setRole(userDTO.getRole());

        var bCryptEncoder = new BCryptPasswordEncoder();
        appUser.setPassword(bCryptEncoder.encode(userDTO.getPassword()));

        AppUser otherUser = userRepository.findByEmail(userDTO.getEmail());
        HashMap<String, Object> existedUser = new HashMap<>();
        existedUser.put("User Already exist",existedUser);
        if (otherUser!=null){
            return existedUser;
        }
        userRepository.save(appUser);

        String jwtToken = jwtService.createJwtToken(appUser);
        HashMap<String, Object> response = new HashMap<>();
        response.put("token",jwtToken);
        response.put("user",appUser);
        return response;

    }
}
