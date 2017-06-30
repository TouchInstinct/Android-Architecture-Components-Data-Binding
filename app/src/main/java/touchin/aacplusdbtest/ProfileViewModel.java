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
package touchin.aacplusdbtest;


import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;

import touchin.aacplusdbtest.utils.LifecycleLiveDataField;
import touchin.aacplusdbtest.utils.TextField;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepository profileRepository;

    // представляет логин авторизованного пользователя или null
    public final ObservableField<String> userLogin;
    // представляет авторизован ли пользователь
    public final ObservableField<Boolean> isUserLoggedIn;
    // представляет логин, введенный пользователем с клавиатуры
    public final TextField inputLogin = new TextField();

    public ProfileViewModel(Application application) {
        super(application);
        profileRepository = ((AacPlusDbTestApp) application).getProfileRepository();
        LiveData<String> loggedInUserLiveData = profileRepository.getLoggedInUser();
        userLogin = new LifecycleLiveDataField<>(loggedInUserLiveData);
        isUserLoggedIn = new LifecycleLiveDataField<>(Transformations.map(loggedInUserLiveData,
                new Function<String, Boolean>() {
                    public Boolean apply(String login) { return login!= null; }
                }));
    }

    public void login() {
        // используем введенный пользователем логин
        if (inputLogin.get() != null) {
            profileRepository.login(inputLogin.get());
        }
    }

    public void logout() { profileRepository.logout(); }
}