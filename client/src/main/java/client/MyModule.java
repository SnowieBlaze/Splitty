/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import client.scenes.*;
import client.utils.ConfigClient;
import client.utils.ServerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

public class MyModule implements Module {

    /**
     * TODO: ...
     * @param binder
     */
    @Override
    public void configure(Binder binder) {
        binder.bind(AddExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditExpenseCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EditParticipantCtrl.class).in(Scopes.SINGLETON);
        binder.bind(AddParticipantCtrl.class).in(Scopes.SINGLETON);
        binder.bind(EventOverviewCtrl.class).in(Scopes.SINGLETON);
        binder.bind(InvitationCtrl.class).in(Scopes.SINGLETON);
        binder.bind(MainCtrl.class).in(Scopes.SINGLETON);
        binder.bind(OpenDebtsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StartScreenCtrl.class).in(Scopes.SINGLETON);
        binder.bind(UserSettingsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(TagCtrl.class).in(Scopes.SINGLETON);
        binder.bind(StatisticsCtrl.class).in(Scopes.SINGLETON);
        binder.bind(ServerUtils.class).in(Scopes.SINGLETON);
        binder.bind(ConfigClient.class).in(Scopes.SINGLETON);
    }
}