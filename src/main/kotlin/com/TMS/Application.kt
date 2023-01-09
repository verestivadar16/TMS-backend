package com.TMS

import com.TMS.plugins.configureMonitoring
import com.TMS.plugins.configureRouting
import com.TMS.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    configureMonitoring()
    configureRouting()
    configureSerialization()

}
