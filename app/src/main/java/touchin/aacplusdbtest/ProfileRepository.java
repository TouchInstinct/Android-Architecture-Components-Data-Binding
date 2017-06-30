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


import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class ProfileRepository {
    private SharedPreferences preferences;
    // подписчики на обновление авторизованного пользователя
    private List<UserUpdateListener> updateListeners = new ArrayList<>();

    public ProfileRepository(Context context) {
        preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public void login(String login) {
        preferences.edit().putString("login", login).apply();
        for (UserUpdateListener listener : updateListeners) {
            listener.update(login);
        }
    }

    public void logout() {
        preferences.edit().putString("login", null).apply();
        for (UserUpdateListener listener : updateListeners) {
            listener.update(null);
        }
    }

    private interface UserUpdateListener {
        void update(String login);
    }

    // возвращает LiveData для отслеживание текущего авторизованного пользователя
    public LiveData<String> getLoggedInUser() { return new LoginLiveData(); }

    private class LoginLiveData extends LiveData<String> implements UserUpdateListener {

        @Override
        // postValue запрашивает обновление на UI-потоке
        // используем, так как мы не уверены, с какого потока будет обновлен логин
        // для немедленного обновления на UI-потоке можно использовать метод setValue
        public void update(String login) { postValue(login); }

        @Override
        // этот метод вызовется, когда на LiveData будет хоть один подписчик,
        // а также объект жизненного цикла этого подписчика будет в состоянии started
        protected void onActive() {
            super.onActive();
            updateListeners.add(this);
            update(preferences.getString("login", null));
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            updateListeners.remove(this);
        }
    }
}
