package model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity
class Cuenta (@PrimaryKey(autoGenerate = true) var id: Int,
              var deuda_total: Float? =0f,
              var deuda_pagada: Float? = 0f)