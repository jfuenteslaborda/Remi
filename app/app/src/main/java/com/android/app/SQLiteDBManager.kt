package com.android.app

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.android.app.models.Medicamento
import com.android.app.models.Usuario
import java.time.LocalDate

class SQLiteDBManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 4 // Incrementa si cambias la estructura
        const val DATABASE_NAME = "MyAppData.db"

        // --- TABLA USUARIO ---
        const val TABLE_USUARIO = "usuario"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_CONTRASENA = "contrasena"
        const val COLUMN_DESCRIPCION = "descripcion"
        const val COLUMN_FECHA_CREACION = "fecha_creacion"
        const val COLUMN_ID_MEDICO = "id_medico"

        // --- TABLA MEDICAMENTO ---
        const val TABLE_MEDICAMENTO = "medicamento"
        const val MEDICAMENTO_ID = "id_medicamento"
        const val MEDICAMENTO_NOMBRE = "nombre"
        const val MEDICAMENTO_DESCRIPCION = "descripcion"
        const val MEDICAMENTO_HORA = "hora"
        const val MEDICAMENTO_TOMADO = "tomado"
        const val MEDICAMENTO_ID_USUARIO = "id_usuario"

        private val USUARIOS_DE_PRUEBA = listOf(
            mapOf(
                COLUMN_NOMBRE to "rbernal",
                COLUMN_DESCRIPCION to "Paciente con problemas respiratorios",
                COLUMN_EMAIL to "rbernal@gmail.com",
                COLUMN_CONTRASENA to "123",
                COLUMN_ID_MEDICO to 2
            ),
            mapOf(
                COLUMN_NOMBRE to "jfuentes",
                COLUMN_DESCRIPCION to "Paciente con el fémur roto",
                COLUMN_EMAIL to "jfuentes@gmail.com",
                COLUMN_CONTRASENA to "123",
                COLUMN_ID_MEDICO to 2
            ),
            mapOf(
                COLUMN_NOMBRE to "rromero",
                COLUMN_DESCRIPCION to "Paciente diabético",
                COLUMN_EMAIL to "rromero@gmail.com",
                COLUMN_CONTRASENA to "123",
                COLUMN_ID_MEDICO to 2
            )
        )

        private val MEDICAMENTOS_DE_PRUEBA = listOf(
            mapOf(
                MEDICAMENTO_NOMBRE to "Ibuprofeno 500mg",
                MEDICAMENTO_DESCRIPCION to "Tomar diluido con agua",
                MEDICAMENTO_HORA to "08:00",
                MEDICAMENTO_TOMADO to 0,
                MEDICAMENTO_ID_USUARIO to 1
            ),
            mapOf(
                MEDICAMENTO_NOMBRE to "Aspirina 100mg",
                MEDICAMENTO_DESCRIPCION to "Tomar con cuidado",
                MEDICAMENTO_HORA to "12:00",
                MEDICAMENTO_TOMADO to 0,
                MEDICAMENTO_ID_USUARIO to 1
            ),
            mapOf(
                MEDICAMENTO_NOMBRE to "Paracetamol 300mg",
                MEDICAMENTO_DESCRIPCION to "Tomar diluido con agua",
                MEDICAMENTO_HORA to "16:00",
                MEDICAMENTO_TOMADO to 1,
                MEDICAMENTO_ID_USUARIO to 2
            ),
            mapOf(
                MEDICAMENTO_NOMBRE to "Vitaminas C",
                MEDICAMENTO_DESCRIPCION to "Recomendable machacar",
                MEDICAMENTO_HORA to "20:00",
                MEDICAMENTO_TOMADO to 0,
                MEDICAMENTO_ID_USUARIO to 2
            ),
            mapOf(
                MEDICAMENTO_NOMBRE to "Antibiotico",
                MEDICAMENTO_DESCRIPCION to "Tomar diluido con agua",
                MEDICAMENTO_HORA to "10:00",
                MEDICAMENTO_TOMADO to 1,
                MEDICAMENTO_ID_USUARIO to 3
            )
        )
    }

    // --- SQL CREATION ---
    private val SQL_CREATE_USUARIO =
        """
        CREATE TABLE $TABLE_USUARIO (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_NOMBRE TEXT NOT NULL,
            $COLUMN_EMAIL TEXT UNIQUE NOT NULL,
            $COLUMN_CONTRASENA TEXT NOT NULL,
            $COLUMN_DESCRIPCION TEXT NOT NULL,
            $COLUMN_ID_MEDICO INTEGER NOT NULL,
            $COLUMN_FECHA_CREACION TEXT
        )
        """.trimIndent()

    private val SQL_CREATE_MEDICAMENTO =
        """
        CREATE TABLE $TABLE_MEDICAMENTO (
            $MEDICAMENTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $MEDICAMENTO_NOMBRE TEXT NOT NULL,
            $MEDICAMENTO_HORA TEXT,
            $MEDICAMENTO_DESCRIPCION TEXT,
            $MEDICAMENTO_TOMADO INTEGER,
            $MEDICAMENTO_ID_USUARIO INTEGER
        )
        """.trimIndent()

    private val SQL_DELETE_USUARIO = "DROP TABLE IF EXISTS $TABLE_USUARIO"
    private val SQL_DELETE_MEDICAMENTO = "DROP TABLE IF EXISTS $TABLE_MEDICAMENTO"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USUARIO)
        db.execSQL(SQL_CREATE_MEDICAMENTO)
        insertarRegistrosAutomaticos(db)
        insertarMedicamentosAutomaticos(db)
        Log.d("SQLiteDBManager", "Tablas creadas y datos de prueba insertados.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_USUARIO)
        db.execSQL(SQL_DELETE_MEDICAMENTO)
        onCreate(db)
    }

    private fun insertarRegistrosAutomaticos(db: SQLiteDatabase) {
        for (userData in USUARIOS_DE_PRUEBA) {
            val values = ContentValues().apply {
                put(COLUMN_NOMBRE, userData[COLUMN_NOMBRE] as String)
                put(COLUMN_ID_MEDICO, userData[COLUMN_ID_MEDICO] as Int)
                put(COLUMN_EMAIL, userData[COLUMN_EMAIL] as String)
                put(COLUMN_DESCRIPCION, userData[COLUMN_DESCRIPCION] as String)
                put(COLUMN_CONTRASENA, userData[COLUMN_CONTRASENA] as String)
                put(COLUMN_FECHA_CREACION, LocalDate.now().toString())
            }
            db.insert(TABLE_USUARIO, null, values)
        }
    }

    private fun insertarMedicamentosAutomaticos(db: SQLiteDatabase) {
        for (medData in MEDICAMENTOS_DE_PRUEBA) {
            val values = ContentValues().apply {
                put(MEDICAMENTO_NOMBRE, medData[MEDICAMENTO_NOMBRE] as String)
                put(MEDICAMENTO_HORA, medData[MEDICAMENTO_HORA] as String)
                put(MEDICAMENTO_DESCRIPCION, medData[MEDICAMENTO_DESCRIPCION] as String)
                put(MEDICAMENTO_TOMADO, medData[MEDICAMENTO_TOMADO] as Int)
                put(MEDICAMENTO_ID_USUARIO, medData[MEDICAMENTO_ID_USUARIO] as Int)
            }
            db.insert(TABLE_MEDICAMENTO, null, values)
        }
    }

    // --- CONSULTAS ---
    fun obtenerTodosLosUsuarios(): List<Usuario> {
        val userList = mutableListOf<Usuario>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIO", null)
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NOMBRE))
                val email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL))
                val descripcion = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                val contrasena = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTRASENA))
                val fechaCreacion = it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA_CREACION))
                val idMedico = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_MEDICO))
                userList.add(Usuario(id, nombre, email, descripcion, contrasena, fechaCreacion, idMedico))
            }
        }
        return userList
    }

    fun obtenerUsuarioPorMedico(medicoId: Int): List<Usuario> {
        val userList = mutableListOf<Usuario>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USUARIO WHERE $COLUMN_ID_MEDICO = ?"
        val cursor = db.rawQuery(query, arrayOf(medicoId.toString()))
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID))
                val nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NOMBRE))
                val email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL))
                val descripcion = it.getString(it.getColumnIndexOrThrow(COLUMN_DESCRIPCION))
                val contrasena = it.getString(it.getColumnIndexOrThrow(COLUMN_CONTRASENA))
                val fechaCreacion = it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA_CREACION))
                val idMedico = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_MEDICO))
                userList.add(Usuario(id, nombre, email, descripcion, contrasena, fechaCreacion, idMedico))
            }
        }
        return userList
    }

    fun obtenerMedicamentosPorUsuario(userId: Int): List<Medicamento> {
        val medicamentoList = mutableListOf<Medicamento>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_MEDICAMENTO WHERE $MEDICAMENTO_ID_USUARIO = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        cursor.use {
            while (it.moveToNext()) {
                val id = it.getInt(it.getColumnIndexOrThrow(MEDICAMENTO_ID))
                val nombre = it.getString(it.getColumnIndexOrThrow(MEDICAMENTO_NOMBRE))
                val hora = it.getString(it.getColumnIndexOrThrow(MEDICAMENTO_HORA))
                val descripcion = it.getString(it.getColumnIndexOrThrow(MEDICAMENTO_DESCRIPCION))
                val tomado = it.getInt(it.getColumnIndexOrThrow(MEDICAMENTO_TOMADO)) == 1
                val idUsuario = it.getInt(it.getColumnIndexOrThrow(MEDICAMENTO_ID_USUARIO))
                medicamentoList.add(Medicamento(id, nombre, hora, descripcion, tomado, idUsuario))
            }
        }
        return medicamentoList
    }

    fun insertarMedicamento(m: Medicamento): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(MEDICAMENTO_NOMBRE, m.nombre)
            put(MEDICAMENTO_HORA, m.hora)
            put(MEDICAMENTO_DESCRIPCION, m.descripcion)
            put(MEDICAMENTO_TOMADO, if (m.tomado) 1 else 0)
            put(MEDICAMENTO_ID_USUARIO, m.idUsuario)
        }
        return db.insert(TABLE_MEDICAMENTO, null, values)
    }
}
