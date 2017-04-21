package no.lagos.mvptest.model

import java.util.*

/**
 * Created by Johannes Dvorak Lagos on 27.02.2017.
 */

class Note {
    lateinit var text: String
     val date: Date by lazy { Date() }

}