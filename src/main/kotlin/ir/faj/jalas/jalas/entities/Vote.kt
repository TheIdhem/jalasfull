package ir.faj.jalas.jalas.entities

import ir.faj.jalas.jalas.enums.VoteType
import org.hibernate.annotations.Type
import java.util.*

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*


@Entity
@Table(name = "ja_vote")
@DynamicUpdate
class Vote(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var Id: Int = 0,

        @Basic
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "creation_time")
        var creationTime: Date = Date(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "option_id", nullable = false)
        var option: SessionOption = SessionOption(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id", nullable = false)
        var user: User = User(),

        @Basic
        @Column(name = "vote_type")
        @Type(type = "ir.faj.jalas.jalas.enums.VoteTypeClassTypeEnum")
        var status: VoteType = VoteType.down


)