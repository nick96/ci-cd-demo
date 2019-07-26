package me.nspain.demo

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
class ConversionControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun getConversionFromKgToLb() {
        val resp = restTemplate.getForEntity<Response>("/conversion?from=kg&to=lb&value=5")
        assert(resp.statusCode.is2xxSuccessful)
        assert(resp.hasBody())

        assertEquals("kg to lb", resp.body!!.conversion)
        assertEquals(11.023113109, resp.body!!.value, 0.02)
    }

    @Test
    fun getConversionFromLbToKg() {
        val resp = restTemplate.getForEntity<Response>("/conversion?from=lb&to=kg&value=5")
        assert(resp.statusCode.is2xxSuccessful)
        assert(resp.hasBody())

        assertEquals("lb to kg", resp.body!!.conversion)
        assertEquals(2.26796185, resp.body!!.value, 0.02)
    }
}