package com.android.app

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ConecctionDB(
    private val url: String = "jdbc:mariadb://localhost:3307/remi",
    private val user: String = "root",
    private val password: String = "123456"
) {

    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(url, user, password)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        }
    }
}
