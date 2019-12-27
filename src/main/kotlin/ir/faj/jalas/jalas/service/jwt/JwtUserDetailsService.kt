package ir.faj.jalas.jalas.service.jwt

import ir.faj.jalas.jalas.entities.repository.UserRepository
import ir.faj.jalas.jalas.exception.NotFoundUser
import org.springframework.beans.factory.annotation.Autowired
import java.util.ArrayList

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository


    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findByUsername(username ?: "khali")
        return if (user != null) {
            User(user.username, user.password,
                    ArrayList())
        } else {
            throw UsernameNotFoundException("User not found with username: $username")
        }
    }
}