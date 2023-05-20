package edu.obya.blueprint.client.config

import org.springframework.web.bind.annotation.ControllerAdvice
import org.zalando.problem.spring.webflux.advice.ProblemHandling
import org.zalando.problem.spring.webflux.advice.security.SecurityAdviceTrait
import org.zalando.problem.spring.webflux.advice.validation.OpenApiValidationAdviceTrait

@ControllerAdvice
class ExceptionHandling : ProblemHandling, OpenApiValidationAdviceTrait, ClientAdviceTrait, SecurityAdviceTrait
