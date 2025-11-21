import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

class Usuario @RequiresApi(Build.VERSION_CODES.O) constructor(
    private var id: Int,
    private var nombre: String,
    private var correo: String,
    private var contraseña: String,
    private var fechaNacimiento: LocalDate
) {
    fun getId(): Int = id
    fun setId(id: Int) { this.id = id }

    fun getNombre(): String = nombre
    fun setNombre(nombre: String) { this.nombre = nombre }

    fun getCorreo(): String = correo
    fun setCorreo(correo: String) { this.correo = correo }

    fun getContraseña(): String = contraseña
    fun setContraseña(contraseña: String) { this.contraseña = contraseña }
}
