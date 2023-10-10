# HotelDelLuna

A Hotel management application for our final OOP project

## Huớng dẫn chạy project mấy friend nghiên cứu nhé, t recommend sử dụng Command Line Line (có hướng dẫn ở dưới)

## Quan trọng là phải cài Gradle

### IntelliJ IDEA

1. Open IntelliJ IDEA and select _File > Open..._.
2. Choose the java-gradle-starter-project directory and click _OK_.
3. Select _File > Project Structure..._ and ensure that the Project SDK and language level are set to use Java 17.
4. Open the Gradle view with _View > Tool Windows > Gradle_.
5. In the Gradle view, double-click `run` under _Tasks > application_ to run the app.

### Eclipse

1. Open Eclipse and select _File > Import_.
2. In the import wizard, choose _Gradle > Existing Gradle Project_, then click _Next_.
3. Select the java-gradle-starter-project directory as the project root directory.
4. Click _Finish_ to complete the import.
5. Select _Project > Properties_ . In _Java Build Path_, ensure that under the Libraries tab, _Modulepath_ is set to JRE System Library (JavaSE-17). In _Java Compiler_, ensure that the _Use compliance from execution environment 'JavaSE-17' on the 'Java Build Path'_ checkbox is selected.
6. Right-click the project in the Project Explorer or Package Explorer and choose _Gradle > Refresh Gradle project_.
7. Open the Gradle Tasks view with _Window > Show View > Other... > Gradle > Gradle Tasks_.
8. In the Gradle Tasks view, double-click `run` under _java-gradle-starter-project > application_ to run the app.

### Command Line

1. Run `gradle clean build`.
2. Run `gradle run`.

## Resources

- [MaterialFx](https://github.com/palexdev/MaterialFX)
- [JavaFx community docs](https://fxdocs.github.io/docs/html5)
- [Java Mongo Driver](https://www.mongodb.com/docs/drivers/java/sync/current/quick-start/)

## Licensing

Copyright 2023 Esri

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy
of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations
under the License.

A copy of the license is available in the repository's license.txt file.
