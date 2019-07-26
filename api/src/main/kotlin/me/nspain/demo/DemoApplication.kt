package me.nspain.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}

@RestController("/conversion")
class ConversionController {

	val conversions = listOf("lb", "kg")

	val TO_KG = 0.45359237
	val TO_LB = 2.20462262185


	@GetMapping("/")
	fun getConversion(@RequestParam from: String, @RequestParam to: String, @RequestParam value: Double): ResponseEntity<Response> {
		if (!conversions.containsAll(listOf(from, to))) {
			return ResponseEntity.badRequest().build()
		}

		val converted = when (from) {
			"kg" -> fromKg(to, value)
			"lb" -> fromLb(to, value)
			else -> { return ResponseEntity.badRequest().build() }
		}!!

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

