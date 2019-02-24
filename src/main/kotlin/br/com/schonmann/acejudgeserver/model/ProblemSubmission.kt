package br.com.schonmann.acejudgeserver.model

import br.com.schonmann.acejudgeserver.model.enum.ProblemSubmissionStatusEnum
import javax.persistence.*

@Entity
class ProblemSubmission(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id : Long,
    @ManyToOne val problem : Problem,
    @ManyToOne val user : User,
    @Lob val solutionProgram : String,
    val status : ProblemSubmissionStatusEnum
)