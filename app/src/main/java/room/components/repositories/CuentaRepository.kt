package room.components.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import model.Cuenta
import room.components.DebitumDataBase
import room.components.daos.CuentaDAO

class CuentaRepository (application: Application) {
    val cuentaDao : CuentaDAO

    init{
        val database = DebitumDataBase.getInstance(application)
        cuentaDao = database.cuentaDao()
    }

    fun insert(cuenta: Cuenta){
        InsertCuentaAsyncTask(cuentaDao).execute(cuenta)
    }

    fun update(cuenta: Cuenta){
        UpdateCuentaAsyncTask(cuentaDao).execute(cuenta)
    }

    fun updateDeudaTotal(monto: Float){
        UpdateDeudaTotalAsyncTask(cuentaDao).execute(monto)
    }

    fun delete(cuenta: Cuenta){
        DeleteCuentaAsyncTask(cuentaDao).execute(cuenta)
    }

    fun deleteAllCuentas(){
        DeleteAllCuentasAsyncTask(cuentaDao).execute()
    }

    fun getAllCuentas() : LiveData<List<Cuenta>>{
        return cuentaDao.getAllCuentas()
    }

    fun getCuenta(id: Int) : LiveData<Cuenta>{
        return cuentaDao.getCuenta(id)
    }

    fun getLastCuentaID() : LiveData<Long>{
        return cuentaDao.getLastAccountID()
    }

    private class InsertCuentaAsyncTask constructor(private val cuentaDao: CuentaDAO) : AsyncTask<Cuenta, Void, Void>(){
        override fun doInBackground(vararg params: Cuenta): Void? {
            cuentaDao.insert(params[0])
            return null
        }
    }

    private class UpdateCuentaAsyncTask constructor(private val cuentaDao: CuentaDAO) : AsyncTask<Cuenta, Void, Void>(){
        override fun doInBackground(vararg params: Cuenta): Void? {
            cuentaDao.update(params[0])
            return null
        }
    }


    private class DeleteCuentaAsyncTask constructor(private val cuentaDao: CuentaDAO) : AsyncTask<Cuenta, Void, Void>(){
        override fun doInBackground(vararg params: Cuenta): Void? {
            cuentaDao.delete(params[0])
            return null
        }
    }

    private class DeleteAllCuentasAsyncTask constructor(private val cuentaDao: CuentaDAO) : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            cuentaDao.deleteAllCuentas()
            return null
        }
    }

    private class UpdateDeudaTotalAsyncTask constructor(private val cuentaDao: CuentaDAO) : AsyncTask<Float,Void, Void>(){
        override fun doInBackground(vararg params: Float?): Void? {
            cuentaDao.updateDeudaTotal(params[0]!!)
            return null
        }

    }


}