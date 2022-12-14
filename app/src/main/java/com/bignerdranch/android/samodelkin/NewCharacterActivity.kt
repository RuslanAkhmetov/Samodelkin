package com.bignerdranch.android.samodelkin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_new_character.*
import kotlinx.coroutines.*

private const val CHARACTER_DATA_KEY = "CHARACTER_DATA_KEY"

private var Bundle.characterData
    get() = getSerializable(CHARACTER_DATA_KEY) as CharacterGenerator.CharacterData
    set(value) = putSerializable(CHARACTER_DATA_KEY, value)



class NewCharacterActivity : AppCompatActivity() {

    private val scope = CoroutineScope(SupervisorJob() + CoroutineName("CHARACTER_API"))

    private var  characterData : CharacterGenerator.CharacterData = CharacterGenerator.generate()

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.characterData = characterData
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        scope.launch {
            characterData = savedInstanceState?.characterData
                ?: fetchCharacterData()
        }

        setContentView(R.layout.activity_new_character)

        generateButton.setOnClickListener {
             scope.launch {
                characterData = fetchCharacterData() //
            }
            displayCharacterData()
        }

        displayCharacterData()

    }

    private fun displayCharacterData() {
        characterData.run {
            nameTextView.text = name
            raceTextView.text = race
            dexterityTextView.text = dex
            wisdomTextView.text = wis
            strengthTextView.text = str
        }
    }

}


