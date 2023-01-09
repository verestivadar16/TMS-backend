package com.TMS.data.table

import org.jetbrains.exposed.sql.Table

object PostTable: Table() {

    val id = varchar("id",512)
    val userEmail = varchar("userEmail", 512).references(UserTable.email)
    val postTitle = text("postTitle")
    val description = text("description")
    val date = long("date")


    override val primaryKey: PrimaryKey = PrimaryKey(id)
}