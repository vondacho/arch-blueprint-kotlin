package edu.software.craftsmanship.blueprint.web.exception

import edu.software.craftsmanship.blueprint.appl.exception.NotFoundException
import mu.KLogger
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status

class InputProblem(
    message: String,
    status: Status = Status.BAD_REQUEST
) : AbstractThrowableProblem(null, status.reasonPhrase, status, message) {

    override fun getCause(): Exceptional? = super.cause
}

fun Throwable.toProblem(logger: KLogger): InputProblem {
    logger.warn(this.message)
    return when (this) {
        is IllegalArgumentException -> this.toBadRequest()
        is IllegalStateException -> this.toBadRequest()
        is NotFoundException -> this.toNotFound()
        else -> this.toInternalError()
    }
}

fun Throwable.toNotFound() = InputProblem(
    message = this.message ?: "",
    status = Status.NOT_FOUND
)

fun Throwable.toBadRequest() = InputProblem(
    message = this.message ?: ""
)

fun Throwable.toInternalError() = InputProblem(
    message = this.message ?: "",
    status = Status.INTERNAL_SERVER_ERROR
)
