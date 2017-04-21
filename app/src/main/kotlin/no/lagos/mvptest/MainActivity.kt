package no.lagos.mvptest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import no.lagos.mvptest.interfaces.MainMVP
import no.lagos.mvptest.presenters.MainPresenter

class MainActivity : AppCompatActivity(), MainMVP.ViewOps {
    override fun enableOTPButton() {
        otp_button.isEnabled = false
    }

    override fun disableOTPButton() {
        otp_button.isEnabled = true
    }

    val presenter: MainMVP.PresenterInterface = MainPresenter(view = this)

    override fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.newNote("Ny melding")

    }
}
