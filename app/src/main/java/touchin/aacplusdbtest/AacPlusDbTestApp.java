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

public class AacPlusDbTestApp extends Application {
    private ProfileRepository profileRepository;

    public ProfileRepository getProfileRepository() {
        return profileRepository;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        profileRepository = new ProfileRepository(this);
    }
}
