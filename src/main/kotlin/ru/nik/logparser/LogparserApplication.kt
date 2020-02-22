package ru.nik.logparser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LogparserApplication

fun main(args: Array<String>) {
    runApplication<LogparserApplication>(*args)
}
