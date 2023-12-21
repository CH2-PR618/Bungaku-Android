package com.nccdms.bloomiqpro.data.dummy

data class Flower(
    val id : Int,
    val nama : String,
    val namaScience:String,
    val jenis : String,
    val asal : List<String>,
    val musim : List<String>,
    val photoUrl: String,
    val description :String
)