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


import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import touchin.aacplusdbtest.utils.LifecycleLiveDataField
import touchin.aacplusdbtest.utils.TextField

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository: ProfileRepository = (application as AacPlusDbTestApp).profileRepository
    // класс Transformations — это класс-хэлпер для преобразования данных
    // метод map, просто конвертирует данные из одного типа в другой - в данном случае из String? в boolean
    private val isUserLoggedInLiveData = Transformations.map(profileRepository.loggedInUser) { login -> login != null }

    // представляет логин авторизованного пользователя или null
    val userLogin = LifecycleLiveDataField(profileRepository.loggedInUser)
    // представляет авторизован ли пользователь
    val isUserLoggedIn = LifecycleLiveDataField(isUserLoggedInLiveData)
    // представляет логин, введенный пользователем с клавиатуры
    // TextField - это ObservableField, реализующий интерфейс TextWatcher
    //    это нужно, чтобы можно было байндиться к text и addTextChangedListener,
    //    организовав таким образом двустронний байндинг.
    // При вводе текста в EditText  изменяется ViewModel, при изменении ViewModel — изменяется в EditText.
    val inputLogin = TextField()

    fun loginOrLogout() {
        // необходимо получить текущее состояние - авторизован пользователь или нет и решить, что делать
        isUserLoggedInLiveData.observeForever(object : Observer<Boolean> {
            override fun onChanged(loggedIn: Boolean?) {
                if (loggedIn!!) {
                    profileRepository.logout()
                } else if (inputLogin.get() != null) {
                    // вызываем логин только если пользователь что-то ввел в поле ввода
                    profileRepository.login(inputLogin.get())
                } else {
                    // по идее, тут можно отобразить ошибку "Введите логин"
                }
                // при выполнении команды приходится отписываться вручную
                isUserLoggedInLiveData.removeObserver(this)
            }
        })
    }
}