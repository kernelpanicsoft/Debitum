package room.components.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import model.Deuda
import room.components.DebitumDataBase
import room.components.daos.DeudaDAO

class DeudaRepository(application: Application) {
    val deudaDao: DeudaDAO

    init{
        val database = DebitumDataBase.getInstance(application)
        deudaDao = database.deudaDao()
    }

    fun insert(deuda: Deuda){
        InsertDeudaAsyncTask(deudaDao).execute(deuda)
    }

    fun update(deuda: Deuda){
        UpdateDeudaAsyncTask(deudaDao).execute(deuda)
    }

    fun updateCuenta(id:Int, monto: Float){
        UpdateMontoDeudaAsyncTask(id,deudaDao).execute(monto)
    }

    fun updateEstatusCuenta(id: Int, estado: Int){
        UpdateEstatusDeudaAsyncTask(id,deudaDao).execute(estado)
    }

    fun delete(deuda: Deuda){
        DeleteDeudaAsyncTask(deudaDao).execute(deuda)
    }

    fun deleteAllDeudas(){
        DeleteAllDeudasAsyncTask(deudaDao).execute()
    }

    fun getAllDeudas() : LiveData<List<Deuda>>{
        return deudaDao.getAllDeudas()
    }

    fun getAllDeudasActivas() : LiveData<List<Deuda>>{
        return deudaDao.getAllDeudasActivas()
    }

    fun getDeuda(id: Int) : LiveData<Deuda>{
        return deudaDao.getDeuda(id)
    }

    fun getAllDeudasByEstado(estado: Int) : LiveData<List<Deuda>>{
        return deudaDao.getDeudasByState(estado)
    }


    private class InsertDeudaAsyncTask constructor(private val deudaDAO: DeudaDAO) : AsyncTask<Deuda, Void, Void>(){
        override fun doInBackground(vararg params: Deuda): Void? {
            deudaDAO.insert(params[0])
            return null
        }
    }

    private class UpdateDeudaAsyncTask constructor(private val deudaDAO: DeudaDAO) : AsyncTask<Deuda, Void, Void>(){
        override fun doInBackground(vararg params: Deuda): Void? {
            deudaDAO.insert(params[0])
            return null
        }
    }

    private class DeleteDeudaAsyncTask constructor(private val deudaDAO: DeudaDAO) : AsyncTask<Deuda, Void, Void>(){
        override fun doInBackground(vararg params: Deuda): Void? {
            deudaDAO.delete(params[0])
            return null
        }
    }

    private class DeleteAllDeudasAsyncTask constructor(private val deudaDAO: DeudaDAO) : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void): Void? {
            deudaDAO.deleteAllDeudas()
            return null
        }
    }

    private class UpdateMontoDeudaAsyncTask constructor(private val id: Int,private val deudaDAO: DeudaDAO) : AsyncTask<Float,Void, Void>(){
        override fun doInBackground(vararg params: Float?): Void? {
            deudaDAO.updateMontoDeuda(id,params[0]!!)
            return null
        }
    }

    private class UpdateEstatusDeudaAsyncTask constructor(private val id: Int, private val deudaDAO: DeudaDAO) : AsyncTask<Int,Void,Void>(){
        override fun doInBackground(vararg params: Int?): Void? {
            deudaDAO.updateStatusDeuda(id,params[0]!!)
            return null
        }
    }

}