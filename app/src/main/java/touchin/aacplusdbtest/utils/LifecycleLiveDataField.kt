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
package touchin.aacplusdbtest.utils


import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.databinding.Observable
import android.databinding.ObservableField
import android.databinding.ViewDataBinding
import android.util.Log
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicInteger

// наследуем от ObservableField
// имплементируем Observer (интерфейс из-подписчик для LiveData) чтобы синхронизировать значения LiveData и ObservableField
class LifecycleLiveDataField<T>(val source: LiveData<T?>) : ObservableField<T>(), Observer<T?> {
    // отслеживаем количество подписчиков на этот ObservableField
    private var observersCount: AtomicInteger = AtomicInteger(0)

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.addOnPropertyChangedCallback(callback)
        try {
            // немножко рефлексии, по-другому никак
            val callbackListenerField = callback.javaClass.getDeclaredField("mListener")
            callbackListenerField.setAccessible(true)
            val callbackListener = callbackListenerField.get(callback) as WeakReference<ViewDataBinding>
            val activity = callbackListener.get()!!.root!!.context as LifecycleActivity
            if (observersCount.incrementAndGet() == 1) {
                source.observe(activity, this)
            }
        } catch (bindingThrowable: Throwable) {
            Log.e("BINDING", bindingThrowable.message)
        }
    }

    override fun onChanged(value: T?) = set(value)

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        super.removeOnPropertyChangedCallback(callback)
        if (observersCount.decrementAndGet() == 0) {
            // отписываемся от LiveData, когда все view отбайндились от ObservableField
            source.removeObserver(this)
        }
    }
}
