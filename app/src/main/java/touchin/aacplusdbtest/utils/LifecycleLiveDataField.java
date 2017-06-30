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
package touchin.aacplusdbtest.utils;


import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableField;
import android.databinding.ViewDataBinding;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

// наследуем от ObservableField
public class LifecycleLiveDataField<T> extends ObservableField<T> {
    private LiveData<T> source;
    // отслеживаем количество подписчиков на этот ObservableField
    private int observersCount;
    // Observer — класс из библиотеки Android Architecture Components
    // подписчик LiveData — прокидывает значения из LiveData в ObservableField
    private Observer<T> observer = new Observer<T>() {
        public void onChanged(T value) { LifecycleLiveDataField.this.set(value); }
    };

    public LifecycleLiveDataField(LiveData<T> source) { this.source = source; }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        try {
            Field callbackListenerField = callback.getClass().getDeclaredField("mListener");
            callbackListenerField.setAccessible(true);
            WeakReference<ViewDataBinding> callbackListener = (WeakReference<ViewDataBinding>) callbackListenerField.get(callback);
            LifecycleActivity activity = (LifecycleActivity) callbackListener.get().getRoot().getContext();

            observersCount++;
            if (observersCount == 1) {
                source.observe(activity, observer);
            }
        } catch (Exception bindingException) {
            Log.e("BINDING", bindingException.getMessage());
        }
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.removeOnPropertyChangedCallback(callback);
        observersCount--;
        if (observersCount == 0) {
            // отписываемся от LiveData, когда все view отбайндились от ObservableField
            source.removeObserver(observer);
        }
    }
}
