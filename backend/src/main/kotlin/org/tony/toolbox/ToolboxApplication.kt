package org.tony.toolbox

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ToolboxApplication

fun main(args: Array<String>) {
    runApplication<ToolboxApplication>(*args)
}
