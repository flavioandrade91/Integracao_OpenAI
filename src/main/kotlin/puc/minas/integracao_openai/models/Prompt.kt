package puc.minas.integracao_openai.models

class Prompt {
    fun build(atores: List<String>, genero: List<String>): String {
        val atoresString = atores.joinToString(", ")
        val generoString = genero.joinToString(", ")
        return "Crie uma sinopse de filme com os atores $atoresString e o gênero $generoString. Sintetize em até 200 tokens."
    }
}