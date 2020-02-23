package ru.nik.logparser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class LogparserApplication

fun main(args: Array<String>) {
    runApplication<LogparserApplication>(*args)
}
