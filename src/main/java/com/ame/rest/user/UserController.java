package com.ame.rest.user;

import java.util.LinkedHashMap;

import com.ame.rest.security.CustomUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping(path = "/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private UserService service;

  // returns true if the user is authenticated
  @RequestMapping("/")
  @ResponseBody
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Boolean> home() {
    return new ResponseEntity<Boolean>(true, HttpStatus.OK);
  }

  // Tokens have a 5 minute expiration. So we give the client the ability to
  // refresh using previous token
  @GetMapping(value = "/refresh")
  @ResponseBody
  @PreAuthorize("isAuthenticated()")
  public String generateNewToken() {

    // get authenticated
    String newToken = service.getJWTToken(userDetailsService.getCurrentUser());

    return newToken;
  }

  @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
  @ResponseBody
  @PreAuthorize("isAnonymous()")
  public ResponseEntity<String> login(@RequestBody LinkedHashMap<String, String> user) {

    String token;
    try {
      token = service.login(user.get("email"), user.get("password"));
      return new ResponseEntity<String>(token, HttpStatus.OK);
    } catch (BadCredentialsException e) {
      return new ResponseEntity<String>("Invalid credentials", HttpStatus.NOT_ACCEPTABLE);
    }

  }

  @PreAuthorize("isAnonymous()")
  @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<String> register(@RequestBody User user) {

    if(!service.validateUserInfo(user)){
      return new ResponseEntity<String>("password or email doesn't meet requirements", HttpStatus.BAD_REQUEST);
    }

    // first check if user already exists
    User currentUser = userRepository.findByEmail(user.getEmail());

    if (currentUser != null) {
      return new ResponseEntity<String>("User already exists", HttpStatus.NOT_ACCEPTABLE);
    }


    // encode the password
    user.setPassword(service.encodePassword(user.getPassword()));
    userRepository.save(user);

    // add default roles
    service.addRoles(user, user.getDefaultRoles());

    return new ResponseEntity<String>("success", HttpStatus.OK);
  }

}
