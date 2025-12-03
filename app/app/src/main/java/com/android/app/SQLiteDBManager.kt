package com.android.app

import com.android.app.models.Usuario
import com.android.app.models.Medicamento
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.provider.FontsContractCompat
import com.android.app.models.Rol
import java.time.LocalDate


class SQLiteDBManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // --- Definiciones de la Base de Datos ---
    companion object {
        // ⚠️ Aumentamos la versión para forzar la recreación de la DB
        // Para que se ejecute onUpgrade y se creen las nuevas tablas (Medicamento)
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "MyAppData.db"

        // === TABLA USUARIO ===
        const val TABLE_USUARIO = "usuario"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_CONTRASENA = "contrasena"
        const val COLUMN_FECHA_CREACION = "fecha_creacion"

        const val COLUMN_ROL= "rol"

        // === TABLA MEDICAMENTO ===s
        const val TABLE_MEDICAMENTO = "medicamento"
        const val MEDICAMENTO_ID = "id_medicamento"
        const val MEDICAMENTO_NOMBRE = "nombre"
        const val MEDICAMENTO_DOSIS = "dosis"
        const val MEDICAMENTO_HORA = "hora"
        const val MEDICAMENTO_TOMADO = "tomado" // 0 o 1
        const val MEDICAMENTO_ID_USUARIO = "id_usuario" // CLAVE FORÁNEA

        // --- REGISTROS DE PRUEBA (ASIGNACIÓN DE ID) ---

        // Los ID se autoincrementan, pero para la asignación en los medicamentos
        // asumimos que el primer usuario tendrá ID=1 y el segundo ID=2, etc.
        private val USUARIOS_DE_PRUEBA = listOf(
            mapOf(COLUMN_NOMBRE to "rbernal", COLUMN_EMAIL to "rbernal@gmail.com", COLUMN_CONTRASENA to "123", COLUMN_ROL to "MEDICO"),
            mapOf(COLUMN_NOMBRE to "jfuentes", COLUMN_EMAIL to "jfuentes@gmail.com", COLUMN_CONTRASENA to "123",COLUMN_ROL to "PACIENTE"),
            mapOf(COLUMN_NOMBRE to "rromero", COLUMN_EMAIL to "rromero@gmail.com", COLUMN_CONTRASENA to "123",COLUMN_ROL to "MEDICO")
        )

        private val MEDICAMENTOS_DE_PRUEBA = listOf(
            // Asignados al Usuario 1 (rbernal)
            mapOf(MEDICAMENTO_NOMBRE to "Ibuprofeno", MEDICAMENTO_DOSIS to "500 mg", MEDICAMENTO_HORA to "08:00", MEDICAMENTO_TOMADO to 0, MEDICAMENTO_ID_USUARIO to 1),
            mapOf(MEDICAMENTO_NOMBRE to "Aspirina", MEDICAMENTO_DOSIS to "100 mg", MEDICAMENTO_HORA to "12:00", MEDICAMENTO_TOMADO to 0, MEDICAMENTO_ID_USUARIO to 1),
            // Asignados al Usuario 2 (jfuentes)
            mapOf(MEDICAMENTO_NOMBRE to "Paracetamol", MEDICAMENTO_DOSIS to "1000 mg", MEDICAMENTO_HORA to "16:00", MEDICAMENTO_TOMADO to 1, MEDICAMENTO_ID_USUARIO to 2),
            mapOf(MEDICAMENTO_NOMBRE to "Vitaminas", MEDICAMENTO_DOSIS to "1 comp.", MEDICAMENTO_HORA to "20:00", MEDICAMENTO_TOMADO to 0, MEDICAMENTO_ID_USUARIO to 2),
            // Asignados al Usuario 3 (rromero)
            mapOf(MEDICAMENTO_NOMBRE to "Antibiotico", MEDICAMENTO_DOSIS to "250 mg", MEDICAMENTO_HORA to "10:00", MEDICAMENTO_TOMADO to 1, MEDICAMENTO_ID_USUARIO to 3)
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

    // --- SQL DELETION STATEMENTS ---

    private val SQL_DELETE_USUARIO = "DROP TABLE IF EXISTS $TABLE_USUARIO"
    private val SQL_DELETE_MEDICAMENTO = "DROP TABLE IF EXISTS $TABLE_MEDICAMENTO"

    // --- MÉTODOS DE SQLITEOPENHELPER ---

    override fun onCreate(db: SQLiteDatabase) {
        // 1. Crear tabla USUARIO
        db.execSQL(SQL_CREATE_USUARIO)
        Log.d("SQLiteDBManager", "✅ Tabla '$TABLE_USUARIO' creada.")

        // 2. Crear tabla MEDICAMENTO
        db.execSQL(SQL_CREATE_MEDICAMENTO)
        Log.d("SQLiteDBManager", "✅ Tabla '$TABLE_MEDICAMENTO' creada.")

        // 3. Insertar Registros Automáticos
        insertarRegistrosAutomaticos(db) // Inserta usuarios
        insertarMedicamentosAutomaticos(db) // Inserta medicamentos y los enlaza a usuarios
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Eliminar y recrear ambas tablas en caso de actualización de versión
        db.execSQL(SQL_DELETE_USUARIO)
        db.execSQL(SQL_DELETE_MEDICAMENTO)
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

        // Consulta filtrada usando WHERE
        val query = "SELECT * FROM $TABLE_MEDICAMENTO WHERE $MEDICAMENTO_ID_USUARIO = ?"

        // El '?' se reemplaza por el valor de userId
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(MEDICAMENTO_ID))
                val nombre = getString(getColumnIndexOrThrow(MEDICAMENTO_NOMBRE))
                val dosis = getString(getColumnIndexOrThrow(MEDICAMENTO_DOSIS))
                val hora = getString(getColumnIndexOrThrow(MEDICAMENTO_HORA))
                // Convierte el INTEGER (0 o 1) a Boolean
                val tomado = getInt(getColumnIndexOrThrow(MEDICAMENTO_TOMADO)) == 1

                medicamentoList.add(Medicamento(id, nombre, dosis, hora, tomado, userId))
            }
        }
        cursor.close()
        return medicamentoList
    }
}