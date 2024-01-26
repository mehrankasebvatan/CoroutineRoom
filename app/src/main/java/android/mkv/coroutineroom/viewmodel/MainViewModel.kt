package android.mkv.coroutineroom.viewmodel

import android.app.Application
import android.mkv.coroutineroom.model.LoginState
import android.mkv.coroutineroom.model.UserDataBase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val userDelete = MutableLiveData<Boolean>()
    val signOut = MutableLiveData<Boolean>()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy { UserDataBase(getApplication()).userDao() }

    fun onSignOut() {
        LoginState.logout()
        signOut.value = true
    }

    fun onDeleteUser() {
        coroutineScope.launch {
            LoginState.user?.let { user ->
                db.deleteUser(user.id)
            }
            withContext(Dispatchers.Main) {
                LoginState.logout()
                userDelete.value = true
            }
        }
    }

}