package android.mkv.coroutineroom.viewmodel

import android.app.Application
import android.mkv.coroutineroom.model.LoginState
import android.mkv.coroutineroom.model.User
import android.mkv.coroutineroom.model.UserDataBase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val loginComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy {
        UserDataBase(getApplication()).userDao()
    }


    fun login(username: String, password: String) {
        coroutineScope.launch {
            val user = db.getUser(username)
            if (user != null) {
                if (password.hashCode() == user.passwordHash){
                    withContext(Dispatchers.Main){
                        LoginState.login(user)
                        loginComplete.value = true
                    }
                } else {
                    withContext(Dispatchers.Main){
                        error.value = "Password not match!"
                    }
                }
            } else {
               withContext(Dispatchers.Main){
                   error.value = "User does`nt exists!"
               }
            }
        }
    }

}