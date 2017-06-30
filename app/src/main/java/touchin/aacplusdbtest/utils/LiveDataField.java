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


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.databinding.ObservableField;

// наследуем от ObservableField
public class LiveDataField<T> extends ObservableField<T> {
    private LiveData<T> source;
    // отслеживаем количество подписчиков на этот ObservableField
    private int observersCount;
    // Observer — класс из библиотеки Android Architecture Components
    // подписчик LiveData — прокидывает значения из LiveData в ObservableField
    private Observer<T> observer = new Observer<T>() {
        public void onChanged(T value) { LiveDataField.this.set(value); }
    };

    public LiveDataField(LiveData<T> source) { this.source = source; }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        observersCount++;
        if (observersCount == 1) {
            // подписываемся на LiveData, когда к ObservableField прибайндивается первая view
            source.observeForever(observer);
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
