package no.lagos.mvptest.presenters

import android.app.Activity
import android.content.Context
import android.util.Log
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.PasswordlessType
import com.auth0.android.callback.BaseCallback
import com.auth0.android.result.Credentials
import com.google.firebase.auth.FirebaseAuth
import no.lagos.mvptest.interfaces.MainMVP
import no.lagos.mvptest.model.MainModel
import no.lagos.mvptest.model.Note
import java.lang.ref.WeakReference

/**
 * Created by Johannes Dvorak Lagos on 27.02.2017.
 */
class MainPresenter(view: MainMVP.ViewOps, val context: Context) : MainMVP.PresenterOps, MainMVP.PresenterInterface {

    val TAG = "Login"
    var view: WeakReference<MainMVP.ViewOps>
    private val model: MainMVP.ModelOps
    private var isChangingConfig = true
    val account: Auth0 by lazy { Auth0(context) }
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    lateinit var jwt: Credentials
    val authentication: AuthenticationAPIClient by lazy { AuthenticationAPIClient(account) }
    lateinit var phoneNumber: String

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
        if (!isChangingConfig) {
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

    override fun onNoteInserted(newNote: Note) {
        view.get()?.showToast("New register added at ${newNote.date}")
    }

    override fun sendPhone(phoneNumber: String) {
        this.phoneNumber = phoneNumber
        authentication.passwordlessWithSMS(phoneNumber, PasswordlessType.CODE)
                .start(object : BaseCallback<Void?, AuthenticationException?> {
                    override fun onFailure(error: AuthenticationException?) {
                        Log.d(TAG, "Login failured ${error?.description}")
                    }

                    override fun onSuccess(payload: Void?) {
                        if (context is Activity) {
                            context.runOnUiThread {
                                view.get()?.enableOTPButton()

                                Log.d(TAG, "Login success $payload")
                            }
                        }


                    }

                })
    }

    override fun sendOTP(otp: String) {

        authentication.loginWithPhoneNumber(phoneNumber, otp)
                .setScope("openid offline_access").setDevice(phoneNumber)
                .start(object : BaseCallback<Credentials, AuthenticationException> {
                    override fun onSuccess(payload: Credentials?) {
                        Log.d(TAG, "PAyload: ${payload.toString()}")
                        Log.d(TAG, "Login in!. Credentials: AccessToken ${payload?.accessToken}, Id Token ${payload?.idToken}, Refresh Token ${payload?.refreshToken}")

                        if(payload != null){
                            jwt = payload
                        }

                        authentication.delegationWithIdToken(jwt.idToken as String, "firebase")
                                .start(object : BaseCallback<Map<String, Any>, AuthenticationException> {
                                    override fun onFailure(error: AuthenticationException?) {
                                        Log.w(TAG, "Something went wrong. $error")
                                    }

                                    override fun onSuccess(payload: Map<String, Any>) {
                                        Log.d(TAG, "Got the payload $payload")
                                        val id_token = payload.get("id_token") as String
                                        auth.signInWithCustomToken(id_token).addOnCompleteListener { task ->
                                            if(task.isSuccessful){
                                                Log.d(TAG, "Successfully registered token ${task.result.user}")
                                            } else {
                                                Log.w(TAG, "Something went wrong. ")
                                            }
                                        }
                                    }

                                })
                    }

                    override fun onFailure(error: AuthenticationException?) {
                        Log.d(TAG, "Failed login ${error.toString()}")
                    }
                })


    }

    override fun wrongOTP(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}