package room.components.daos

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import model.Cuenta

@Dao
interface CuentaDAO {
    @Insert
    fun insert(cuenta: Cuenta)

    @Update
    fun update(cuenta: Cuenta)

    @Delete
    fun delete(cuenta: Cuenta)

    @Query("DELETE FROM Cuenta")
    fun deleteAllCuentas()

    @Query("SELECT * FROM Cuenta")
    fun getAllCuentas(): LiveData<List<Cuenta>>

    @Query("SELECT * FROM Cuenta WHERE Cuenta.id = :id")
    fun getCuenta(id: Int?) : LiveData<Cuenta>

}