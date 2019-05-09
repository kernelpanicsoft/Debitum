package room.components.repositories

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import model.Pago
import room.components.DebitumDataBase
import room.components.daos.PagoDAO

class PagoRepository (application: Application) {
    val pagoDao : PagoDAO

    init {
        val database = DebitumDataBase.getInstance(application)
        pagoDao = database.pagoDao()
    }

    fun insert(pago: Pago){
        InsertPagoAsyncTask(pagoDao).execute(pago)
    }

    fun update(pago: Pago){
        UpdatePagoAsyncTask(pagoDao).execute(pago)
    }

    fun delete(pago: Pago){
        DeletePagoAsyncTask(pagoDao).execute(pago)
    }

    fun deleteAllPAgos(){
        DeleteAllPagosAsyncTask(pagoDao).execute()
    }

    fun getPago(id: Int) : LiveData<Pago>{
        return pagoDao.getPago(id)
    }

    fun getAllPagos() : LiveData<List<Pago>>{
        return pagoDao.getAllPagos()
    }

    private class InsertPagoAsyncTask constructor(private val pagoDAO: PagoDAO) : AsyncTask<Pago, Void, Void>(){
        override fun doInBackground(vararg params: Pago): Void? {
            pagoDAO.insert(params[0])
            return null
        }
    }

    private class UpdatePagoAsyncTask constructor(private val pagoDAO: PagoDAO) : AsyncTask<Pago, Void, Void>(){
        override fun doInBackground(vararg params: Pago): Void? {
            pagoDAO.update(params[0])
            return null
        }
    }

    private class DeletePagoAsyncTask constructor(private val pagoDAO: PagoDAO) : AsyncTask<Pago, Void, Void>(){
        override fun doInBackground(vararg params: Pago): Void? {
            pagoDAO.delete(params[0])
            return null
        }
    }

    private class DeleteAllPagosAsyncTask constructor(private val pagoDAO: PagoDAO) : AsyncTask<Void, Void, Void>(){
        override fun doInBackground(vararg params: Void?): Void? {
            pagoDAO.deleteAllPagos()
            return null
        }
    }
}