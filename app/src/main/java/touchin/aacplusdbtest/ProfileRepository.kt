/*
 *  Copyright (c) 2017 Touch Instinct
 *
 *  This file is sample project file.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package touchin.aacplusdbtest


import android.arch.lifecycle.LiveData
import android.content.Context

class ProfileRepository(context: Context) {
    private val loginKey = "login"
    private val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    // LiveData, на которую можно подписаться
    // и получать обновления логина пользователя
    private val innerLoggedInUser = LoggedInUserLiveData()

    val loggedInUser: LiveData<String?>
        get() = innerLoggedInUser

    fun login(login: String) {
        preferences.edit().putString(loginKey, login).apply()
        notifyAboutUpdate(login)
    }

    fun logout() {
        preferences.edit().putString(loginKey, null).apply()
        notifyAboutUpdate(null)
    }

    private fun notifyAboutUpdate(login: String?) {
        innerLoggedInUser.update(login)
    }

    private inner class LoggedInUserLiveData : LiveData<String?>() {

        // так лучше не делать в конструкторе, а высчитывать текщее значение асинхронно
        // при первом вызове колбека onActive
        init {
            value = preferences.getString(loginKey, null)
        }

        // postValue запрашивает обновление на UI-потоке
        // используем, так как мы не уверены, с какого потока будет обновлен логин
        // для немедленного обновления на UI-потоке можно использовать метод setValue
        fun update(login: String?) {
            postValue(login)
        }
    }
}
