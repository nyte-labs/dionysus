package com.nytelabs.dionysus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DionysusApplication

fun main(args: Array<String>) {
    runApplication<DionysusApplication>(*args)
}
