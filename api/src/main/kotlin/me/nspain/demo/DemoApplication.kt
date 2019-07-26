package me.nspain.demo

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
open class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@RestController
@RequestMapping("/conversion")
class ConversionController {

	val conversions = listOf("lb", "kg")

	val TO_KG = 0.45359237
	val TO_LB = 2.20462262185

	val logger = LoggerFactory.getLogger(this::class.java)


	@GetMapping
	fun getConversion(@RequestParam from: String, @RequestParam to: String, @RequestParam value: Double): ResponseEntity<Response> {
		logger.info("Received request with from='$from', to='$to' and value='$value'")
		if (!conversions.containsAll(listOf(from, to))) {
			logger.info("Invalid value for from or to")
			return ResponseEntity.badRequest().build()
		}

		val converted = when (from) {
			"kg" -> fromKg(to, value)
			"lb" -> fromLb(to, value)
			else -> { return ResponseEntity.badRequest().build() }
		}!!
		logger.info("Converted $value$from to $converted$to")

		val resp = Response("$from to $to", converted)
		return ResponseEntity.ok(resp)
	}

	private fun fromLb(to: String, value: Double): Double? {
		return when (to) {
			"kg" -> value * TO_KG
			else -> null
		}
	}

	private fun fromKg(to: String, value: Double): Double? {
		return when (to) {
			"lb" -> value * TO_LB
			else -> null
		}	}
}

data class Response(val conversion: String, val value: Double)

