package com.TMS.repository

import com.TMS.data.model.Post
import com.TMS.data.model.User
import com.TMS.data.table.PostTable
import com.TMS.data.table.UserTable
import com.TMS.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class Repo {

    suspend fun addUser(user: User){
        dbQuery{
            UserTable.insert { ut ->
                ut[UserTable.email] = user.email
                ut[UserTable.hashPassword] = user.hashPassword
                ut[UserTable.name] = user.userName
            }
        }
    }

    suspend fun findUserByEmail(email:String) = dbQuery{
        UserTable.select{ UserTable.email.eq(email) }
            .map { rowToUser(it)}
            .singleOrNull()
    }

    private fun rowToUser(row:ResultRow?): User?{
        if(row == null){
            return null
        }

        return User(
            email = row[UserTable.email],
            hashPassword = row[UserTable.hashPassword],
            userName = row[UserTable.name]
        )
    }

    // --- Posts ---

    suspend fun addPost(post: Post, email:String){
        dbQuery {
            PostTable.insert { nt->
                nt[PostTable.id] = post.id
                nt[PostTable.userEmail] = email
                nt[PostTable.postTitle] = post.title
                nt[PostTable.description] = post.description
                nt[PostTable.date] = post.date
            }
        }

    }

    suspend fun updatePost(post:Post, email:String){
        dbQuery {
            PostTable.update(
                where ={
                    PostTable.userEmail.eq((email)) and PostTable.id.eq(post.id)
                }
            ) {nt->
                nt[PostTable.postTitle] = post.title
                nt[PostTable.description] = post.description
                nt[PostTable.date] = post.date
            }
        }
    }

    suspend fun deletePost(id:String, email: String){
        dbQuery {
            PostTable.deleteWhere { PostTable.userEmail.eq(email) and PostTable.id.eq(id) }
        }
    }

    suspend fun getAllPosts(email:String):List<Post> = dbQuery {

        PostTable.select{
            PostTable.userEmail.eq(email)
        }.mapNotNull { rowToPost(it) }
    }

    private fun rowToPost(row:ResultRow?): Post?{
        if(row == null){
            return null
        }

        return Post(
            id = row[PostTable.id],
            title = row[PostTable.postTitle],
            description = row[PostTable.description],
            date = row[PostTable.date]
        )
    }

}