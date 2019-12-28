package ir.faj.jalas.jalas.controllers

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import ir.faj.jalas.jalas.configurations.JwtTokenUtil
import ir.faj.jalas.jalas.controllers.model.JwtRequest
import ir.faj.jalas.jalas.controllers.model.JwtResponse
import ir.faj.jalas.jalas.controllers.model.LoginResponse
import ir.faj.jalas.jalas.exception.NotFoundPassword
import ir.faj.jalas.jalas.exception.NotFoundUser
import ir.faj.jalas.jalas.service.jwt.JwtUserDetailsService
import ir.faj.jalas.jalas.service.user.UserService
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping("/api/v1.0")
class JwtAuthenticationController(private val authenticationManager: AuthenticationManager,
                                  private val userService: UserService) {


    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil;

    @Autowired
    lateinit var userDetailsService: JwtUserDetailsService;

    @PostMapping("/login")
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtRequest): ResponseEntity<JwtResponse> {
        authenticationRequest.username ?: throw NotFoundUser()
        authenticationRequest.password ?: throw NotFoundPassword()
        authenticate(authenticationRequest.username as String, authenticationRequest.password as String)

        var userDetails: UserDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.username)

        var token = jwtTokenUtil.generateToken(userDetails)

        return ResponseEntity.ok(JwtResponse(token, userService.getUserInfo(userDetails.username)))

    }

    private fun authenticate(username: String, password: String) {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: DisabledException) {
            throw Exception("USER_DISABLED", e);
        } catch (e: BadCredentialsException) {
            throw Exception("INVALID_CREDENTIALS", e);
        }
    }
}