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

class SignupViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val db by lazy {
        UserDataBase(getApplication()).userDao()
    }

    val signupComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val allList = MutableLiveData<MutableList<User>>()

    fun signup(username: String, password: String, info: String) {

        coroutineScope.launch {
            val user = db.getUser(username)
            if (user != null) {
                withContext(Dispatchers.Main) {
                    error.value = "User already exists!"
                }
            } else {
                val user = User(username, password.hashCode(), info)
                val userId = db.insertUser(user)
                user.id = userId
                LoginState.login(user)
                withContext(Dispatchers.Main) {
                    signupComplete.value = true
                }
            }
        }

    }

    fun getList() {
        var list = mutableListOf<User>()
        coroutineScope.launch {
            list = db.allList()
            if (!list.isEmpty()){
                withContext(Dispatchers.Main){
                    allList.value = list
                }
            } else {
                withContext(Dispatchers.Main){
                    allList.value = mutableListOf()
                }
            }
        }
    }


}