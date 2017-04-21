package no.lagos.mvptest.interfaces

import no.lagos.mvptest.model.Note

/**
 * Created by Johannes Dvorak Lagos on 27.02.2017.
 */
interface MainMVP {

    /*
    *   Presenter -> View
     */
    interface ViewOps {
        fun showToast(msg: String)
        fun enableOTPButton()
        fun disableOTPButton()
    }

    /*
    * View -> Presenter
     */
    interface PresenterInterface {
        fun onConfigurationChanged(view: ViewOps)
        fun onDestroy(isChangingConfig: Boolean)
        fun newNote(textToNote: String)
        fun deleteNote(note: Note)
        fun sendPhone(phoneNumber: String)
        fun sendOTP(otp: String)
    }

    /*
    * Model -> Presenter
     */
    interface PresenterOps {
        fun onNoteInserted(newNote: Note)
        fun onNoteRemoved(removedNote: Note)
        fun onError(errorMsg: String)
        fun wrongOTP(msg: String)

    }

    /*
    *  Presenter -> Model
     */
    interface ModelOps {
        fun insertNote(note: Note)
        fun removeNote(note: Note)
        fun authorizeWithPhone(phone: String)
        fun respondWithOtp(otp: String)
        fun onDestroy()
    }
}