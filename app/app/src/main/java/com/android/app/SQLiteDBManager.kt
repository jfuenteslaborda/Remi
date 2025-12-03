package com.android.app

import com.android.app.models.Usuario
import com.android.app.Medicamento.Medicamento
import com.android.app.models.Cita // Añadido
import com.android.app.models.estado // Añadido
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.android.app.models.Rol
import java.time.LocalDate


class SQLiteDBManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // --- Definiciones de la Base de Datos ---
    companion object {
        // ⚠️ VERSIÓN FINAL: Aumentada a 4 para crear la tabla Cita
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "MyAppData.db"

        // === TABLA USUARIO ===
        const val TABLE_USUARIO = "usuario"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_CONTRASENA = "contrasena"
        const val COLUMN_FECHA_CREACION = "fecha_creacion"
        const val COLUMN_ROL= "rol"

        // === TABLA MEDICAMENTO ===
        const val TABLE_MEDICAMENTO = "medicamento"
        const val MEDICAMENTO_ID = "id_medicamento"
        const val MEDICAMENTO_NOMBRE = "nombre"
        const val MEDICAMENTO_DOSIS = "dosis"
        const val MEDICAMENTO_HORA = "hora"
        const val MEDICAMENTO_TOMADO = "tomado" // 0 o 1
        const val MEDICAMENTO_ID_USUARIO = "id_usuario" // CLAVE FORÁNEA

        // === TABLA CITA (¡COMPLETO!) ===
        const val TABLE_CITA = "cita"
        const val CITA_ID = "id_cita"
        const val CITA_TITULO = "titulo"
        const val CITA_FECHA = "fecha"
        const val CITA_HORA = "hora"
        const val CITA_DOCTOR = "doctor"
        const val CITA_ESTADO = "estado" // Guardamos el ENUM como TEXT
        const val CITA_ID_USUARIO = "id_usuario" // CLAVE FORÁNEA

        // --- REGISTROS DE PRUEBA (ASIGNACIÓN DE ID) ---

        private val USUARIOS_DE_PRUEBA = listOf(
            mapOf(COLUMN_NOMBRE to "rbernal", COLUMN_EMAIL to "rbernal@gmail.com", COLUMN_CONTRASENA to "123", COLUMN_ROL to "MEDICO"),
            mapOf(COLUMN_NOMBRE to "jfuentes", COLUMN_EMAIL to "jfuentes@gmail.com", COLUMN_CONTRASENA to "123",COLUMN_ROL to "PACIENTE"),
            mapOf(COLUMN_NOMBRE to "rromero", COLUMN_EMAIL to "rromero@gmail.com", COLUMN_CONTRASENA to "123",COLUMN_ROL to "MEDICO")
        )

        private val MEDICAMENTOS_DE_PRUEBA = listOf(
            // Asignados al Usuario 2 (jfuentes - PACIENTE, para pruebas de calendario)
            mapOf(MEDICAMENTO_NOMBRE to "Ibuprofeno", MEDICAMENTO_DOSIS to "500 mg", MEDICAMENTO_HORA to "08:00", MEDICAMENTO_TOMADO to 0, MEDICAMENTO_ID_USUARIO to 2),
            mapOf(MEDICAMENTO_NOMBRE to "Aspirina", MEDICAMENTO_DOSIS to "100 mg", MEDICAMENTO_HORA to "12:00", MEDICAMENTO_TOMADO to 0, MEDICAMENTO_ID_USUARIO to 2),
            mapOf(MEDICAMENTO_NOMBRE to "Paracetamol", MEDICAMENTO_DOSIS to "1000 mg", MEDICAMENTO_HORA to "16:00", MEDICAMENTO_TOMADO to 1, MEDICAMENTO_ID_USUARIO to 2),
            mapOf(MEDICAMENTO_NOMBRE to "Vitaminas", MEDICAMENTO_DOSIS to "1 comp.", MEDICAMENTO_HORA to "20:00", MEDICAMENTO_TOMADO to 0, MEDICAMENTO_ID_USUARIO to 2)
        )

        private val CITAS_DE_PRUEBA = listOf(
            // Asignadas al Usuario 2 (jfuentes - PACIENTE)
            mapOf(CITA_TITULO to "Cardiología", CITA_FECHA to "2025-12-04", CITA_HORA to "10:30", CITA_DOCTOR to "Dr. Alberto Pérez", CITA_ESTADO to estado.Aceptada.name, CITA_ID_USUARIO to 2),
            mapOf(CITA_TITULO to "Dermatología", CITA_FECHA to "2025-12-05", CITA_HORA to "15:00", CITA_DOCTOR to "Dra. Laura Gómez", CITA_ESTADO to estado.Pendiente.name, CITA_ID_USUARIO to 2)
        )
    }

    // --- SQL CREATION STATEMENTS ---

    private val SQL_CREATE_USUARIO =
        "CREATE TABLE $TABLE_USUARIO (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NOMBRE TEXT NOT NULL," +
                "$COLUMN_EMAIL TEXT UNIQUE NOT NULL," +
                "$COLUMN_CONTRASENA TEXT NOT NULL," +
                "$COLUMN_FECHA_CREACION TEXT," +
                "$COLUMN_ROL TEXT NOT NULL)"

    private val SQL_CREATE_MEDICAMENTO =
        "CREATE TABLE $TABLE_MEDICAMENTO (" +
                "$MEDICAMENTO_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$MEDICAMENTO_NOMBRE TEXT NOT NULL," +
                "$MEDICAMENTO_DOSIS TEXT," +
                "$MEDICAMENTO_HORA TEXT," +
                "$MEDICAMENTO_TOMADO INTEGER," +
                "$MEDICAMENTO_ID_USUARIO INTEGER)" // Columna de clave foránea

    private val SQL_CREATE_CITA =
        "CREATE TABLE $TABLE_CITA (" +
                "$CITA_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$CITA_TITULO TEXT NOT NULL," +
                "$CITA_FECHA TEXT NOT NULL," +
                "$CITA_HORA TEXT NOT NULL," +
                "$CITA_DOCTOR TEXT," +
                "$CITA_ESTADO TEXT NOT NULL," + // Guardamos el ENUM como TEXTO
                "$CITA_ID_USUARIO INTEGER)"

    // --- SQL DELETION STATEMENTS ---

    private val SQL_DELETE_USUARIO = "DROP TABLE IF EXISTS $TABLE_USUARIO"
    private val SQL_DELETE_MEDICAMENTO = "DROP TABLE IF EXISTS $TABLE_MEDICAMENTO"
    private val SQL_DELETE_CITA = "DROP TABLE IF EXISTS $TABLE_CITA"

    // --- MÉTODOS DE SQLITEOPENHELPER ---

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Crear tabla USUARIO
        db.execSQL(SQL_CREATE_USUARIO)
        Log.d("SQLiteDBManager", "✅ Tabla '$TABLE_USUARIO' creada.")

        // 2. Crear tabla MEDICAMENTO
        db.execSQL(SQL_CREATE_MEDICAMENTO)
        Log.d("SQLiteDBManager", "✅ Tabla '$TABLE_MEDICAMENTO' creada.")

        // 3. Crear tabla CITA
        db.execSQL(SQL_CREATE_CITA)
        Log.d("SQLiteDBManager", "✅ Tabla '$TABLE_CITA' creada.")

        // 4. Insertar Registros Automáticos
        insertarRegistrosAutomaticos(db)
        insertarMedicamentosAutomaticos(db)
        insertarCitasAutomaticas(db) // ¡Llamada a la nueva función!
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar y recrear todas las tablas
        db.execSQL(SQL_DELETE_USUARIO)
        db.execSQL(SQL_DELETE_MEDICAMENTO)
        db.execSQL(SQL_DELETE_CITA) // ¡Añadido!
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    // --- LÓGICA DE INSERCIÓN DE REGISTROS AUTOMÁTICOS ---

    private fun insertarRegistrosAutomaticos(db: SQLiteDatabase) {
        var insertedCount = 0
        Log.d("SQLiteDBManager", "⏳ Insertando ${USUARIOS_DE_PRUEBA.size} usuarios predefinidos...")

        for (userData in USUARIOS_DE_PRUEBA) {
            val values = ContentValues().apply {
                put(COLUMN_NOMBRE, userData[COLUMN_NOMBRE])
                put(COLUMN_EMAIL, userData[COLUMN_EMAIL])
                put(COLUMN_CONTRASENA, userData[COLUMN_CONTRASENA])
                put(COLUMN_FECHA_CREACION, LocalDate.now().toString())
                put(COLUMN_ROL, userData[COLUMN_ROL])
            }

            val newRowId = db.insert(TABLE_USUARIO, null, values)

            if (newRowId != -1L) {
                insertedCount++
            } else {
                Log.e("SQLiteDBManager", "❌ Error al insertar el usuario: ${userData[COLUMN_NOMBRE]}")
            }
        }
        Log.d("SQLiteDBManager", "✅ Usuarios insertados: $insertedCount.")
    }

    fun obtenerCitaPorId(citaId: Int): Cita? {
        val db = this.readableDatabase
        var cita: Cita? = null

        // Consulta SQL para obtener una sola fila por ID
        val query = "SELECT * FROM $TABLE_CITA WHERE $CITA_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(citaId.toString()))

        with(cursor) {
            if (moveToFirst()) { // Solo esperamos una fila, así que moveToFirst es suficiente.
                val id = getInt(getColumnIndexOrThrow(CITA_ID))
                val titulo = getString(getColumnIndexOrThrow(CITA_TITULO))
                val fecha = getString(getColumnIndexOrThrow(CITA_FECHA))
                val hora = getString(getColumnIndexOrThrow(CITA_HORA))
                val doctor = getString(getColumnIndexOrThrow(CITA_DOCTOR))
                val estadoStr = getString(getColumnIndexOrThrow(CITA_ESTADO))
                val idUsuario = getInt(getColumnIndexOrThrow(CITA_ID_USUARIO))

                // Convertir String a Enum
                val estadoCita = try {
                    estado.valueOf(estadoStr)
                } catch (e: IllegalArgumentException) {
                    estado.Pendiente
                }

                cita = Cita(id, titulo, fecha, hora, doctor, estadoCita, idUsuario)
            }
        }
        cursor.close()
        return cita
    }

    private fun insertarMedicamentosAutomaticos(db: SQLiteDatabase) {
        var insertedCount = 0
        Log.d("SQLiteDBManager", "⏳ Insertando ${MEDICAMENTOS_DE_PRUEBA.size} medicamentos predefinidos...")

        for (medicamentoData in MEDICAMENTOS_DE_PRUEBA) {
            val values = ContentValues().apply {
                put(MEDICAMENTO_NOMBRE, medicamentoData[MEDICAMENTO_NOMBRE] as String)
                put(MEDICAMENTO_DOSIS, medicamentoData[MEDICAMENTO_DOSIS] as String)
                put(MEDICAMENTO_HORA, medicamentoData[MEDICAMENTO_HORA] as String)
                put(MEDICAMENTO_TOMADO, medicamentoData[MEDICAMENTO_TOMADO] as Int)
                put(MEDICAMENTO_ID_USUARIO, medicamentoData[MEDICAMENTO_ID_USUARIO] as Int)
            }

            val newRowId = db.insert(TABLE_MEDICAMENTO, null, values)

            if (newRowId != -1L) {
                insertedCount++
            } else {
                Log.e("SQLiteDBManager", "❌ Error al insertar el medicamento: ${medicamentoData[MEDICAMENTO_NOMBRE]}")
            }
        }
        Log.d("SQLiteDBManager", "✅ Medicamentos insertados: $insertedCount.")
    }

    // FUNCIÓN DE INSERCIÓN DE CITAS
    private fun insertarCitasAutomaticas(db: SQLiteDatabase) {
        var insertedCount = 0
        Log.d("SQLiteDBManager", "⏳ Insertando ${CITAS_DE_PRUEBA.size} citas predefinidas...")

        for (citaData in CITAS_DE_PRUEBA) {
            val values = ContentValues().apply {
                put(CITA_TITULO, citaData[CITA_TITULO] as String)
                put(CITA_FECHA, citaData[CITA_FECHA] as String)
                put(CITA_HORA, citaData[CITA_HORA] as String)
                put(CITA_DOCTOR, citaData[CITA_DOCTOR] as String)
                put(CITA_ESTADO, citaData[CITA_ESTADO] as String) // Guardamos el nombre del ENUM
                put(CITA_ID_USUARIO, citaData[CITA_ID_USUARIO] as Int)
            }
            db.insert(TABLE_CITA, null, values)
            insertedCount++
        }
        Log.d("SQLiteDBManager", "✅ Citas insertadas: $insertedCount.")
    }

    // --- LÓGICA DE LECTURA DE DATOS ---

    fun obtenerTodosLosUsuarios(): List<Usuario> {
        val userList = mutableListOf<Usuario>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIO", null)

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val nombre = getString(getColumnIndexOrThrow(COLUMN_NOMBRE))
                val email = getString(getColumnIndexOrThrow(COLUMN_EMAIL))
                val contrasena = getString(getColumnIndexOrThrow(COLUMN_CONTRASENA))
                val fechaCreacion = getString(getColumnIndexOrThrow(COLUMN_FECHA_CREACION))
                val rolString = getString(getColumnIndexOrThrow(COLUMN_ROL))
                val rol = Rol.valueOf(rolString)

                userList.add(Usuario(id, nombre, email, contrasena, fechaCreacion,rol))
            }
        }
        cursor.close()
        return userList
    }

    /**
     * Recupera todos los medicamentos asociados a un ID de usuario específico.
     */
    fun obtenerMedicamentosPorUsuario(userId: Int): List<Medicamento> {
        val medicamentoList = mutableListOf<Medicamento>()
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_MEDICAMENTO WHERE $MEDICAMENTO_ID_USUARIO = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(MEDICAMENTO_ID))
                val nombre = getString(getColumnIndexOrThrow(MEDICAMENTO_NOMBRE))
                val dosis = getString(getColumnIndexOrThrow(MEDICAMENTO_DOSIS))
                val hora = getString(getColumnIndexOrThrow(MEDICAMENTO_HORA))
                val tomado = getInt(getColumnIndexOrThrow(MEDICAMENTO_TOMADO)) == 1

                medicamentoList.add(Medicamento(id, nombre, dosis, hora, tomado, userId))
            }
        }
        cursor.close()
        return medicamentoList
    }

    // FUNCIÓN DE LECTURA DE CITAS
    fun obtenerTodasLasCitasPorUsuario(userId: Int): List<Cita> {
        val citaList = mutableListOf<Cita>()
        val db = this.readableDatabase

        val query = "SELECT * FROM $TABLE_CITA WHERE $CITA_ID_USUARIO = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(CITA_ID))
                val titulo = getString(getColumnIndexOrThrow(CITA_TITULO))
                val fecha = getString(getColumnIndexOrThrow(CITA_FECHA))
                val hora = getString(getColumnIndexOrThrow(CITA_HORA))
                val doctor = getString(getColumnIndexOrThrow(CITA_DOCTOR))
                val estadoStr = getString(getColumnIndexOrThrow(CITA_ESTADO))

                val estadoCita = try {
                    estado.valueOf(estadoStr)
                } catch (e: IllegalArgumentException) {
                    estado.Pendiente
                }

                citaList.add(Cita(id, titulo, fecha, hora, doctor, estadoCita, userId))
            }
        }
        cursor.close()
        return citaList
    }

    // FUNCIÓN DE ACTUALIZACIÓN DE MEDICAMENTOS (MARCAR COMO TOMADO)
    fun marcarMedicamentoComoTomado(medicamentoId: Int): Int {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(MEDICAMENTO_TOMADO, 1) // 1 = true
        }

        val selection = "$MEDICAMENTO_ID = ?"
        val selectionArgs = arrayOf(medicamentoId.toString())

        val count = db.update(
            TABLE_MEDICAMENTO,
            values,
            selection,
            selectionArgs
        )

        return count
    }
}