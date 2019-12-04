package room.components.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import android.os.AsyncTask
import model.JoinDeudaRecordatorio
import model.RecordatorioPago
import room.components.DebitumDataBase
import room.components.daos.RecordatorioPagoDAO

class RecordatorioPagoRepository (application: Application) {
    val recordatorioPagoDAO : RecordatorioPagoDAO

    init{
        val database = DebitumDataBase.getInstance(application)
        recordatorioPagoDAO = database.recordatorioPagoDao()
    }

    fun insert(recordatorioPago: RecordatorioPago){
        InsertRecordatorioPagoAsyncTask(recordatorioPagoDAO).execute(recordatorioPago)
    }

    fun update(recordatorioPago: RecordatorioPago){
        UpdateRecordatorioPagoAsyncTask(recordatorioPagoDAO).execute(recordatorioPago)
    }

    fun getSumaRecordatorios() : LiveData<Float>{
        return recordatorioPagoDAO.getSumaRecordatorios()
    }

    fun getSumaRecordatoriosSemanales() : LiveData<Float>{
        return recordatorioPagoDAO.getSumaPagosSemanales()
    }

    fun getSumaRecordatorioPagos() : LiveData<Float>{
        return recordatorioPagoDAO.getSumaPagos()
    }

    fun delete(recordatorioPago: RecordatorioPago){
        DeleteRecordatorioPagoAsyncTask(recordatorioPagoDAO).execute(recordatorioPago)
    }

    fun deleteAllRecordatoriosPago(){
        DeleteAllRecordatoriosPagoAsyncTask(recordatorioPagoDAO).execute()
    }

    fun getRecordatorioPago(id: Int) : LiveData<RecordatorioPago>{
        return recordatorioPagoDAO.getRecordatorioPago(id)
    }

    fun getAllRecordatoriosPago() : LiveData<List<RecordatorioPago>>{
        return recordatorioPagoDAO.getAllRecordatoriosPago()
    }

    fun getRecordatoriosPagoDeuda() : LiveData<List<JoinDeudaRecordatorio>>{
        return recordatorioPagoDAO.getRecordatoriosPagoDeuda()
    }

    fun getRecordatoriosDia() : LiveData<List<RecordatorioPago>>{
        return recordatorioPagoDAO.getRemindersOfTheDay()
    }

    private class InsertRecordatorioPagoAsyncTask constructor(private val recordatorioPagoDAO: RecordatorioPagoDAO) : AsyncTask<RecordatorioPago, Void, Void>(){
        override fun doInBackground(vararg params: RecordatorioPago): Void? {
            recordatorioPagoDAO.insert(params[0])
            return null
        }
    }

    private class UpdateRecordatorioPagoAsyncTask constructor(private val recordatorioPagoDAO: RecordatorioPagoDAO) : AsyncTask<RecordatorioPago, Void, Void>(){
        override fun doInBackground(vararg params: RecordatorioPago): Void? {
            recordatorioPagoDAO.update(params[0])
            return null
        }
    }

    private class DeleteRecordatorioPagoAsyncTask constructor(private val recordatorioPagoDAO: RecordatorioPagoDAO) : AsyncTask<RecordatorioPago, Void, Void>(){
        override fun doInBackground(vararg params: RecordatorioPago): Void? {
            recordatorioPagoDAO.delete(params[0])
            return null
        }
    }

    private class DeleteAllRecordatoriosPagoAsyncTask constructor(private val recordatorioPagoDAO: RecordatorioPagoDAO) : AsyncTask<RecordatorioPago, Void, Void>(){
        override fun doInBackground(vararg params: RecordatorioPago): Void? {
            recordatorioPagoDAO.deleteAllRecordatoriosPago()
            return null
        }
    }
}