package room.components

import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import model.*
import room.components.daos.CuentaDAO
import room.components.daos.DeudaDAO
import room.components.daos.PagoDAO
import room.components.daos.RecordatorioPagoDAO
import room.components.viewModels.CuentaViewModel

@Database(entities = arrayOf(Cuenta::class,
    Deuda::class,
    Pago::class,
    RecordatorioPago::class),
    version = 1)
abstract class DebitumDataBase : RoomDatabase() {
    abstract fun cuentaDao(): CuentaDAO
    abstract fun deudaDao(): DeudaDAO
    abstract fun pagoDao(): PagoDAO
    abstract fun recordatorioPagoDao(): RecordatorioPagoDAO

    companion object{
        @Volatile private var instance: DebitumDataBase? = null

        fun getInstance(context: Context) : DebitumDataBase =
                instance ?: synchronized(this){
                    instance ?: buildDatabase(context).also { instance = it}
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                    DebitumDataBase::class.java, "Debitum.db")
                   /* .addCallback( object : RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            instance?.cuentaDao()?.insert(Cuenta(0,0f))
                        }
                    }

                    )*/
                    .build()
    }
}