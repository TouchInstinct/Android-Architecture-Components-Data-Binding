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


import android.databinding.ObservableField
import android.text.Editable
import android.text.TextWatcher

class TextField : ObservableField<String>(), TextWatcher {
    override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(text: Editable) {
        if (text.toString() != get()) {
            set(text.toString())
        }
    }
}
