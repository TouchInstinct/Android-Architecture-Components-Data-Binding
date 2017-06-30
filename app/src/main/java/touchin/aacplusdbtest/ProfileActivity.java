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

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import touchin.aacplusdbtest.databinding.ActivityProfileBinding;

// наследуем от LifecycleActivity, так как это может понадобиться для LiveData.
// LiveData будет активироваться, когда эта активити будет в состоянии started.
public class ProfileActivity extends LifecycleActivity {
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // инициализируем байндинг
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        // устанавливаем ViewModel для байндинга
        ProfileViewModel profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding.setProfileViewModel(profileViewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.setProfileViewModel(null);
        binding.executePendingBindings();
    }

}