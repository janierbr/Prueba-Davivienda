# TaskApp - Dashboard de Tareas Colaborativas (Senior Coding Challenge)

Este proyecto es una aplicación de gestión de tareas móvil desarrollada con **React Native CLI** y **TypeScript**, diseñada bajo un enfoque **Offline-First**. Cumple con requisitos avanzados de arquitectura limpia, persistencia local con Realm y componentes de UI nativos bridgeados.

## 🚀 Características Principales

*   **Arquitectura Offline-First**: Sincronización transparente entre la API de `dummyjson.com/todos` y la base de datos local **Realm**.
*   **Estado Global**: Gestión eficiente con **Zustand**.
*   **Componentes Nativos**: `AvatarView` (Android/iOS) implementado en Kotlin y Swift para optimizar el rendimiento visual.
*   **Módulo de Cámara (Plus)**: Bridge nativo funcional para captura de fotos.
*   **Diseño Premium**: Interfaz moderna con animaciones sutiles y feedback instantáneo.

---

## 🛠️ Stack Tecnológico

*   **Framework**: React Native CLI (0.74+)
*   **Lenguaje**: TypeScript (Strict Mode)
*   **Persistencia**: Realm Database (Elegido por su performance superior en transacciones masivas y mapeo directo de objetos).
*   **Estado**: Zustand (Elegido por su simplicidad y bajo boilerplate comparado con Redux).
*   **Navegación**: React Navigation (Stack).

---

## 📖 Uso de IA (Documentación Obligatoria)

Este proyecto fue desarrollado en colaboración con el asistente **Antigravity (DeepMind)**. La supervisión humana fue crítica en los siguientes puntos:

1.  **Troubleshooting del Entorno**: Durante la fase de construcción, se detectaron incompatibilidades entre la versión de Gradle (9.0) y el Java runtime del sistema. Se propuso un downgrade estratégico a **Gradle 8.13** y la configuración manual de `JAVA_HOME` para garantizar un entorno reproducible.
2.  **Arquitectura del Bridge**: Se utilizó la IA para generar el "skeleton" de los archivos Swift para iOS y Kotlin para Android, asegurando que las firmas de los métodos cumplieran con los estándares de React Native.
3.  **Manejo de Errores en Sincronización**: La lógica de "Pull-to-Refresh" fue optimizada para evitar condiciones de carrera durante la escritura en Realm.

---

## 📋 Requisitos de Instalación

1.  **Node.js**: >= 20.x
2.  **Java Development Kit (JDK)**: OpenJDK 21.
3.  **Android Studio**: Configurado con SDK 34 o superior.
4.  **Entorno**: Asegúrate de tener configurado `ANDROID_HOME`.

---

## 🚀 Cómo Ejecutar el Proyecto

1.  **Clonar y configurar**:
    ```bash
    npm install
    ```

2.  **Ejecutar en Android**:
    Si tienes un emulador abierto y el puerto 8081 ocupado:
    ```bash
    $env:JAVA_HOME = "Ruta\A\Tu\JDK"; $env:PATH = "$env:JAVA_HOME\bin;$env:PATH"; npm run android -- --port 8082
    ```

3.  **Pruebas Unitarias**:
    ```bash
    npm test
    ```

---

## 🏛️ Arquitectura Detallada

El proyecto sigue una estructura **Clean Architecture**:

*   `src/domain`: Entidades puras e interfaces de repositorios.
*   `src/data`: Implementaciones (Realm, API) y mapeadores.
*   `src/presentation`: Componentes React, Screens y Zustand Stores.
*   `android/ios`: Implementaciones nativas bridgeadas.
