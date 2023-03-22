package com.github.haskiro.FirstSecurityApp.controllers;

import com.github.haskiro.FirstSecurityApp.dto.AuthenticationDTO;
import com.github.haskiro.FirstSecurityApp.dto.PersonDTO;
import com.github.haskiro.FirstSecurityApp.models.Person;
import com.github.haskiro.FirstSecurityApp.security.JWTUtil;
import com.github.haskiro.FirstSecurityApp.services.RegisrationService;
import com.github.haskiro.FirstSecurityApp.utils.ErrorResponse;
import com.github.haskiro.FirstSecurityApp.utils.PersonException;
import com.github.haskiro.FirstSecurityApp.utils.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static com.github.haskiro.FirstSecurityApp.utils.ErrorUtil.returnErrorsAsString;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final PersonValidator personValidator;
    private final RegisrationService regisrationService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(PersonValidator personValidator,
                          RegisrationService regisrationService,
                          JWTUtil jwtUtil, ModelMapper modelMapper) {
        this.personValidator = personValidator;
        this.regisrationService = regisrationService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "auth/login";
//    }
//
//    @GetMapping("/registration")
//    public String registrationPage(@ModelAttribute("person") Person person) {
//        return "auth/registration";
//    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO,
                                                      BindingResult bindingResult) {

        Person person = convertToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new PersonException(errorMessage);
        }

        regisrationService.register(person);

        String token = jwtUtil.generateToken(person.getUsername());

        return Map.of("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),
                        authenticationDTO.getPassword());

        authenticationManager.authenticate(authInputToken);

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());

        return Map.of("jwt-token", token);
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return this.modelMapper.map(personDTO, Person.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PersonException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadCredentialsException e) {
        ErrorResponse response = new ErrorResponse(
                "Incorrect credentials",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
