package puc.minas.integracao_openai

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import puc.minas.integracao_openai.models.Prompt
import puc.minas.integracao_openai.openai.OpenAI

data class RequestPayload(
	val atores: List<String>,
	val genero: List<String>
)

data class ResponseBody(val sinopse: String)

@RestController
class OpenAIController(private val restTemplate: RestTemplate) {

	@PostMapping("/criarSinopse")
	fun criarSinopse(
		@RequestBody requestBody: RequestPayload,
		@RequestHeader("Authorization") authorization: String
	): ResponseEntity<ResponseBody> {
		val prompt = Prompt().build(requestBody.atores, requestBody.genero)
		val completion = OpenAI().getCompletion(prompt, authorization)

		return if (completion != null) {
			ResponseEntity.ok(ResponseBody(completion))
		} else {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
		}
	}
}

@SpringBootApplication
class IntegracaoOpenaiApplication {
	@Bean
	fun restTemplate(): RestTemplate {
		return RestTemplate()
	}
}

fun main(args: Array<String>) {
	runApplication<IntegracaoOpenaiApplication>(*args)
}
