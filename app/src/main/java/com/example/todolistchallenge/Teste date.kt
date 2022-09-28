package com.example.todolistchallenge

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.BitSet.valueOf

@RequiresApi(Build.VERSION_CODES.O)
val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val locale = Locale("pt", "BR")
    val data :SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy", locale)
    val localDate = LocalDate.now()

    val dataAtual: String = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(localDate)

    println(dataAtual)
    println()

}

private val sdf = SimpleDateFormat("")

val date = Date()



//val localDate = LocalDate.now()

