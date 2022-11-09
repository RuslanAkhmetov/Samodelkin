package com.bignerdranch.android.samodelkin

import kotlinx.coroutines.*
import java.io.Serializable
import java.net.URL

private const val CHARACTER_DATA_API = "https://chargen-api.herokuapp.com/"

private fun <T> List<T>.rand() = shuffled().first()

private fun Int.roll() = (0 until this).sumOf { (1..6).toList().rand() }
    .toString()

private val firstName = listOf("Eli", "Alex", "Sophie")

private val secondName = listOf("Lightweaver", "Greatfoot", "Oakenfeld")



object CharacterGenerator {
    data class CharacterData(
        val name: String,
        val race: String,
        val dex: String,
        val wis: String,
        val str: String,
    ) : Serializable


    fun fromApiData (apiData: String) : CharacterData{
        val(name, race, dex, wis, str) = apiData.split(",")
        return CharacterData(name, race, dex, wis, str)
    }

    private fun name() = "${firstName.rand()} ${secondName.rand()}"
    private fun race() = listOf("dwarf", "elf", "human", "halfling")
        .rand()

    private fun dex() = 4.roll()

    private fun wis() = 3.roll()

    private fun str() = 5.roll()

    fun generate() = CharacterData(
        name = name(),
        race = race(),
        dex = dex(),
        wis = wis(),
        str = str()
    )
}

suspend fun fetchCharacterData() : CharacterGenerator.CharacterData {
            return withContext(Dispatchers.IO){
                var apiData: String
                do {
                    apiData = URL(CHARACTER_DATA_API).readText()
                } while(CharacterGenerator.fromApiData(apiData).str.toInt() < 10 )
                CharacterGenerator.fromApiData(apiData)
            }
}




