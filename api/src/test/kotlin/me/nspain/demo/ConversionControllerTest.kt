package me.nspain.demo

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.test.context.junit4.SpringRunner
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConversionControllerTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @org.junit.jupiter.api.Test
    fun getConversionFromKgToLb() {
        val resp = restTemplate.getForEntity<Response>("/conversion?from=kg&to=lb&value=5")
        assertEquals(200, resp.statusCode)
        assert(resp.hasBody())

        assertEquals("kg to lb", resp.body!!.conversion)
        assertEquals(11.023113109, resp.body!!.value, 0.02)
    }

    @org.junit.jupiter.api.Test
    fun getConversionFromLbToKg() {
        val resp = restTemplate.getForEntity<Response>("/conversion?from=lb&to=kg&value=5")
        assertEquals(200, resp.statusCode)
        assert(resp.hasBody())

        assertEquals("lb to kg", resp.body!!.conversion)
        assertEquals(2.26796185, resp.body!!.value, 0.02)
    }
}