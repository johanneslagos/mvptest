package no.lagos.mvptest.presenters

import no.lagos.mvptest.interfaces.MainMVP
import no.lagos.mvptest.model.MainModel
import no.lagos.mvptest.model.Note
import java.lang.ref.WeakReference

/**
 * Created by Johannes Dvorak Lagos on 27.02.2017.
 */
class MainPresenter(view: MainMVP.ViewOps) : MainMVP.PresenterOps, MainMVP.PresenterInterface {
    override fun sendPhone(phoneNumber: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendOTP(otp: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun wrongOTP(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var view: WeakReference<MainMVP.ViewOps>
    private  val model: MainMVP.ModelOps
     private var isChangingConfig = true

    init {
        this.view = WeakReference<MainMVP.ViewOps>(view)
        this.model = MainModel(presenter = this)
    }

    override fun onNoteRemoved(removedNote: Note) {
        view.get()?.showToast("Note removed")
    }

    override fun onError(errorMsg: String) {
        view.get()?.showToast(errorMsg)
    }

    override fun onConfigurationChanged(view: MainMVP.ViewOps) {
        this.view = WeakReference<MainMVP.ViewOps>(view)
    }

    override fun onDestroy(isChangingConfig: Boolean) {
        this.isChangingConfig = isChangingConfig
        if(!isChangingConfig ){
            model.onDestroy()
        }
    }

    override fun newNote(textToNote: String) {
        val note = Note()
        note.text = textToNote
        model.insertNote(note)
    }

    override fun deleteNote(note: Note) {
        model.removeNote(note)
    }

    override fun onNoteInserted(newNote:Note) {
        view.get()?.showToast("New register added at ${newNote.date}")
    }


}