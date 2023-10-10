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
  requires org.mongodb.driver.core;
  requires org.mongodb.driver.sync.client;
  requires org.mongodb.bson;

  requires jdk.localedata;

  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.graphics;
  requires javafx.media;

  opens com.ht.hoteldelluna;
  opens com.ht.hoteldelluna.controllers;

  exports com.ht.hoteldelluna;
}
