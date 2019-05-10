package room.components.viewModels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import model.Cuenta
import room.components.repositories.CuentaRepository


class CuentaViewModel (application: Application) : AndroidViewModel(application){
    val repository : CuentaRepository = CuentaRepository(application)

    fun insert(cuenta: Cuenta){
        repository.insert(cuenta)
    }

    fun update(cuenta: Cuenta){
        repository.update(cuenta)
    }

    fun delete(cuenta: Cuenta){
        repository.delete(cuenta)
    }

    fun deleteAllCuentas(){
        repository.deleteAllCuentas()
    }

    fun getCuenta(id : Int) : LiveData<Cuenta>{
        return repository.getCuenta(id)
    }

    fun getAllCuentas() : LiveData<List<Cuenta>>{
        return repository.getAllCuentas()
    }
}