package com.matag.game

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class MatagGameApplication

fun main(args: Array<String>) {
    runApplication<MatagGameApplication>(*args)
}