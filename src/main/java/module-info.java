/*
 * Copyright 2022 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

module com.ht.hoteldelluna {
    // modules required by the app
    requires MaterialFX;
    requires VirtualizedFX;

    requires jdk.localedata;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires jfxtras.controls;
    requires jfxtras.common;
    requires jfxtras.fxml;
    requires java.sql;

    opens com.ht.hoteldelluna;
    opens com.ht.hoteldelluna.models;
    opens com.ht.hoteldelluna.controllers.main;
    opens com.ht.hoteldelluna.controllers.test;
    opens com.ht.hoteldelluna.controllers.auth;
    
    exports com.ht.hoteldelluna;
    exports com.ht.hoteldelluna.enums;
    opens com.ht.hoteldelluna.controllers.main.RoomManager;
    opens com.ht.hoteldelluna.controllers.main.roomSetting;
    opens com.ht.hoteldelluna.controllers.main.FloorSetting;
    opens com.ht.hoteldelluna.controllers.main.RoomTypeSetting;
}
