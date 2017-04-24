package no.lagos.mvptest.model

import no.lagos.mvptest.interfaces.MainMVP

/**
 * Created by Johannes Dvorak Lagos on 27.02.2017.
 */
class MainModel(presenter: MainMVP.PresenterOps) : MainMVP.ModelOps {
    override fun authorizeWithPhone(phone: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun respondWithOtp(otp: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val presenter : MainMVP.PresenterOps

    init {
        this.presenter = presenter
    }
    override fun insertNote(note: Note) {
        presenter.onNoteInserted(note)
    }

    override fun removeNote(note: Note) {
        presenter.onNoteRemoved(note)
    }

    override fun onDestroy() {
    }
}