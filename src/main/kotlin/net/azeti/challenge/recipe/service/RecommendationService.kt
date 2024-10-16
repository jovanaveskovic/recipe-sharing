package net.azeti.challenge.recipe.service

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class RecommendationService(
    @Value("\${recipe.recommendation.location}")
    private val location: String,
    @Value("\${recipe.recommendation.apiKey}")
    private val apiKey: String,
    private val jsonMapper: ObjectMapper
) {

    fun getCurrentTemperature(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val current = LocalDateTime.now().format(formatter).toString()
        val uri =
            URI("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$location/$current?unitGroup=metric&key=$apiKey&contentType=json")
        val json = uri.toURL().readText()
        jsonMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        return jsonMapper.readValue(json, TemperatureResponse::class.java).getTemperatures()[0].temp
    }
}

internal class TemperatureResponse @JsonCreator constructor(@JsonProperty("days") private val temps: List<Temperature>) {

    fun getTemperatures(): List<Temperature> {
        return temps
    }
}

internal class Temperature @JsonCreator constructor(@param:JsonProperty("temp") val temp: String) {
    override fun toString(): String {
        return temp
    }
}