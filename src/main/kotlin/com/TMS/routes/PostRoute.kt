package com.TMS.routes

import com.TMS.data.model.Post
import com.TMS.data.model.SimpleResponse
import com.TMS.data.model.User
import com.TMS.repository.Repo
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val POSTS = "$API_VERSION/posts"
const val CREATE_POSTS = "$POSTS/create"
const val UPDATE_POSTS = "$POSTS/update"
const val DELETE_POSTS = "$POSTS/delete"

fun Route.PostRoute(
    db:Repo,
    hashFunction: (String) -> String
){
    authenticate("jwt") {
        post(CREATE_POSTS){
            val post = try {
                call.receive<Post>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,e.message ?:"Missing Fields"))
                return@post
            }

            try{
                val email = call.principal<User>()!!.email
                db.addPost(post,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Post Added Successfully!"))
            }catch(e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?:"Some Problem Occurred"))
            }
        }

        get(POSTS) {
            try {
                val email = call.principal<User>()!!.email
                val posts = db.getAllPosts(email)
                call.respond(HttpStatusCode.OK,posts)
            }catch (e:java.lang.Exception){
                call.respond(HttpStatusCode.Conflict, e.message ?:"Some Problem Occurred")
            }
        }

        post(UPDATE_POSTS){
            val post = try {
                call.receive<Post>()
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false,"Missing Fields"))
                return@post
            }

            try{
                val email = call.principal<User>()!!.email
                db.updatePost(post,email)
                call.respond(HttpStatusCode.OK,SimpleResponse(true,"Post Updated Successfully!"))
            }catch(e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false,e.message ?:"Some Problem Occured"))
            }
        }

        delete(DELETE_POSTS) {
            val postId = try{
                call.request.queryParameters["id"]!!
            }catch (e:Exception){
                call.respond(HttpStatusCode.BadRequest,SimpleResponse(false, e.message ?: "QueryParameters: id is not present"))
                return@delete
            }
            try {
                val email = call.principal<User>()!!.email
                db.deletePost(email,postId)
                call.respond(HttpStatusCode.OK,SimpleResponse(true, "Post Deleted Successfully!"))
            }catch (e:Exception){
                call.respond(HttpStatusCode.Conflict,SimpleResponse(false, e.message ?: "Some problem Occurred when Deleting The Post"))
            }
        }

    }
}
