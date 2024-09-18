package com.aps.cafe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.Environment

@SpringBootApplication
class CafeApplication

fun main(args: Array<String>) {
    runApplication<CafeApplication>(*args)
}
