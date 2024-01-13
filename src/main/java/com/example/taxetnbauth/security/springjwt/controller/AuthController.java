package com.example.taxetnbauth.security.springjwt.controller;


import com.example.taxetnbauth.security.springjwt.models.*;
import com.example.taxetnbauth.security.springjwt.payload.request.LoginRequest;
import com.example.taxetnbauth.security.springjwt.payload.request.SignupRequest;
import com.example.taxetnbauth.security.springjwt.payload.request.UpdateProfileRequest;
import com.example.taxetnbauth.security.springjwt.payload.response.JwtResponse;
import com.example.taxetnbauth.security.springjwt.payload.response.MessageResponse;
import com.example.taxetnbauth.security.springjwt.repository.RoleRepository;
import com.example.taxetnbauth.security.springjwt.repository.UserRepository;
import com.example.taxetnbauth.security.springjwt.security.jwt.JwtUtils;
import com.example.taxetnbauth.security.springjwt.security.jwt.services.UserDetailsImpl;
import com.example.taxetnbauth.service.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;





@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  String uri = "http://localhost:8050/api/terain";


  @Autowired
  RestTemplate restTemplate;


  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());
    
    System.out.println(jwt+"   "+userDetails.getUsername());

    return ResponseEntity.ok(new JwtResponse(jwt,
                         userDetails.getId(), 
                         userDetails.getUsername(),
                         userDetails.getFirstName(),
                         userDetails.getLastName(),

                         roles));
    
    
  }
  
  @PutMapping("/users/{userId}/password")

  public ResponseEntity<?> updatePassword(
      @PathVariable Long userId, @Valid @RequestBody ChangePasswordRequest updatePasswordRequest) {

    User user = userRepository.getReferenceById(userId);

    if (!encoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid old password");
    }

    user.setPassword(encoder.encode(updatePasswordRequest.getNewPassword()));
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
  }
  
  @GetMapping("/users")
  @PreAuthorize("permitAll()")
  public ResponseEntity<List<User>> findAllUsers() {
      List<User> users = userRepository.findAll();
      return ResponseEntity.ok(users);
  }
  
  @GetMapping("/users/admin")
  @PreAuthorize("permitAll()")
  public ResponseEntity<List<User>> findAdmin() {
      List<User> users = userRepository.findAdmin();
      return ResponseEntity.ok(users);
  }
  
  @GetMapping("/users/mod")
  @PreAuthorize("permitAll()")
  public ResponseEntity<List<User>> findMod() {
      List<User> users = userRepository.findMod();
      return ResponseEntity.ok(users);
  }
  
  @GetMapping("/users/role/{username}")
  @PreAuthorize("permitAll()")
  public ResponseEntity<ERole> findRole(@PathVariable String username) {
	  ERole role = userRepository.findRole(username);
      return ResponseEntity.ok(role);
  }
  
  @GetMapping("/users/user")
  @PreAuthorize("permitAll()")
  public ResponseEntity<List<User>> findUser() {
      List<User> users = userRepository.findUser();
      return ResponseEntity.ok(users);
  }



  @PutMapping("/updateprofile/{username}")
  public ResponseEntity<?> updateUserProfile(@Valid @RequestBody UpdateProfileRequest signupRequest, @PathVariable String username) {
    // Retrieve the current authenticated user
   // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUsername = username;
    User currentUser = userRepository.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("User not found with username: " + currentUsername));

    // Update user information
    currentUser.setFirstName(signupRequest.getFirstName());
    currentUser.setLastName(signupRequest.getLastName());
    currentUser.setUsername(signupRequest.getUsername());

    if(!encoder.matches(signupRequest.getOldPassword(), currentUser.getPassword())){
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Old password is incorrect."));
    }
    // Update password if provided
    if (!signupRequest.getNewPassword().equals("")) {

      if (encoder.matches(signupRequest.getOldPassword(), currentUser.getPassword())) {
        currentUser.setPassword(encoder.encode(signupRequest.getNewPassword()));
      } else {
        return ResponseEntity.badRequest().body(new MessageResponse("Error: Old password is incorrect."));
      }
    }

    // Save the updated user
    userRepository.save(currentUser);



    return ResponseEntity.ok(new MessageResponse("User profile updated successfully!"));
  }



  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

   /* if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }*/

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getCin(),

               signUpRequest.getFirstName(),

            signUpRequest.getLastName(),
            signUpRequest.getEmail(),
               encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    Redevable redevable = new Redevable();
    redevable.setCin(user.getCin());
    redevable.setPrenom(user.getFirstName());
    redevable.setNom(user.getLastName());

    String uri2 = "http://localhost:8050/api/redevable";

    restTemplate.postForEntity(uri2 + "/save", redevable, Redevable.class);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
