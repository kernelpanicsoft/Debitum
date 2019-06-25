package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("UPDATE Cuenta SET deuda_total = deuda_total+:monto" )
    fun updateDeudaTotal( monto: Float)

    @Query("SELECT max(id) FROM Cuenta")
    fun getLastAccountID() : LiveData<Long>

}