package puc.minas.integracao_openai.openai

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.client.RestTemplate

class OpenAI {
    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    public fun getCompletion(prompt: String, authorization: String): String? {
        val endpoint = "https://api.openai.com/v1/chat/completions"
        val token = authorization.split(" ").getOrNull(1) ?: return null

        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            setBearerAuth(token)
        }

        val request = HttpEntity(buildRequestBody(prompt), headers)
        val response = restTemplate().postForEntity(endpoint, request, String::class.java)

        return if (response.statusCode == HttpStatus.OK) {
            response.body?.let { extractContent(it) }
        } else {
            null
        }
    }

    private fun buildRequestBody(prompt: String): String {
        return """
            {
                "messages": [{"role": "user", "content": "$prompt"}],
                "max_tokens": 200,
                "temperature": 0.7,
                "model": "gpt-3.5-turbo"
            }
        """.trimIndent()
    }

    private fun extractContent(completion: String): String {
        val jsonResponse = ObjectMapper().readTree(completion)
        return jsonResponse["choices"][0]["message"]["content"].asText()
    }
}