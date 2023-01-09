package com.TMS.plugins

import com.TMS.Authentification.JwtService
import com.TMS.Authentification.hash
import com.TMS.repository.DatabaseFactory
import com.TMS.repository.Repo
import com.TMS.routes.PostRoute
import com.TMS.routes.UserRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*


fun Application.configureRouting() {

    DatabaseFactory.init()
    val db = Repo()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    install(Authentication) {
        jwt("jwt") {
            verifier(jwtService.verifier)
            realm = "Post Server"

            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                user
            }

        }
    }
    //install(Locations)

    routing {
        UserRoutes(db, jwtService, hashFunction)
        PostRoute(db, hashFunction)


        // Static plugin. Try to access `/static/index.html`
        static {
            resources("static")
        }
    }

}
