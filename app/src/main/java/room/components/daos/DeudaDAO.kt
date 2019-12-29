package room.components.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import model.Deuda
import helpcodes.EstatusDeuda
import model.CantidadTipoDeuda

@Dao
interface DeudaDAO {
    @Insert
    fun insert(deuda: Deuda)

    @Update
    fun update(deuda: Deuda)

    @Delete
    fun delete(deuda: Deuda)

    @Query("DELETE FROM Deuda")
    fun deleteAllDeudas()

    @Query("SELECT * FROM Deuda")
    fun getAllDeudas(): LiveData<List<Deuda>>

    @Query("SELECT * FROM Deuda WHERE Deuda.id = :id")
    fun getDeuda(id: Int): LiveData<Deuda>

    @Query("UPDATE Deuda SET pagado = pagado+:monto WHERE Deuda.id = :id")
    fun updateMontoDeuda(id: Int, monto: Float)

    @Query("SELECT * FROM DEUDA WHERE Deuda.estado = :estado")
    fun getDeudasByState(estado: Int) : LiveData<List<Deuda>>

    @Query("SELECT * FROM Deuda WHERE Deuda.estado = " + EstatusDeuda.ACTIVA + " OR Deuda.estado =" + EstatusDeuda.SEGUIR)
    fun getAllDeudasActivas() : LiveData<List<Deuda>>

    @Query("UPDATE Deuda SET estado = :estado WHERE Deuda.id = :id")
    fun updateStatusDeuda(id: Int, estado: Int)

    @Query("UPDATE Deuda SET monto = :montoNuevo WHERE Deuda.id = :id")
    fun updateMontoOriginal(id: Int, montoNuevo: Float)

    @Query("SELECT (SUM(monto) - SUM(pagado)) FROM DEUDA")
    fun getSumaDeudas() : LiveData<Float>

    @Query("SELECT Deuda.tipo, COUNT(Deuda.id) as cantidad, SUM(Deuda.monto) - SUM(Deuda.pagado) as deudaPorTipo FROM Deuda GROUP BY Deuda.tipo")
    fun getCuentaTiposDeuda() : LiveData<List<CantidadTipoDeuda>>


}