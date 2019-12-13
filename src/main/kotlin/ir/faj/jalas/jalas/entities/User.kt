package ir.faj.jalas.jalas.entities

import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*


@Entity
@Table(name = "ja_user")
@DynamicUpdate
class User(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Int = 0,

        @Basic
        @Column(name = "name", nullable = false)
        var name: String = "",

        @Basic
        @Column(name = "email", nullable = false)
        var email: String = "",

        @Basic
        @Column(name = "username", nullable = false)
        var username: String = "",

        @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
        var sessions: MutableList<Session> = mutableListOf(),

        @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        @Fetch(value = FetchMode.SELECT)
        var votes: List<Vote> = listOf()

)