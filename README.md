# TaskNexus - Prueba Técnica Davivienda

**TaskNexus** es una aplicación móvil premium desarrollada en React Native, diseñada para la gestión eficiente de tareas con un enfoque **Offline-First**. La aplicación permite listar tareas sincronizadas desde una API, editarlas, eliminarlas y adjuntar fotografías capturadas directamente desde un módulo de cámara nativo.

---

## 🚀 Instalación y Ejecución Rápida

Para facilitar la verificación de la prueba, se ha automatizado todo el proceso de arranque (configuración de Java, encendido de emulador y despliegue).

### Requisitos Previos
*   **Android Studio** instalado con un AVD (emulador) configurado.
*   **Node.js** (v22 o superior recomendado).

### Pasos para Ejecutar
1.  Abre una terminal en la carpeta principal del proyecto.
2.  Navega a la carpeta de la app:
    ```powershell
    cd mobile_app
    ```
3.  Ejecuta el script de automatización (PowerShell):
    ```powershell
    powershell -ExecutionPolicy Bypass -File .\fast-run.ps1
    ```

> [!TIP]
> El script se encargará de configurar el `JAVA_HOME` por ti, abrir el emulador si no está encendido, esperar a que Android arranque e instalar la aplicación automáticamente.

---

## 🏗️ Arquitectura Offline-First: ¿Por qué Realm?

Para este proyecto se seleccionó **Realm** como motor de persistencia local en lugar de WatermelonDB por las siguientes razones clave:

1.  **Modelo de Objetos Nativo**: Realm permite definir esquemas como clases de TypeScript, lo que facilita el tipado fuerte y la integridad de los datos.
2.  **Reactividad en Tiempo Real**: Realm ofrece "Live Collections". Cuando un dato cambia en la base de datos, la UI se actualiza automáticamente sin necesidad de recargar manualmente el estado.
3.  **Rendimiento**: Al ser una base de datos NoSQL diseñada específicamente para móviles, ofrece una velocidad de lectura/escritura superior a la de los wrappers sobre SQLite.
4.  **Enfoque Offline**: La aplicación está diseñada para que el usuario pueda interactuar con sus tareas (ver, editar, borrar, tomar fotos) sin conexión. Los cambios se guardan localmente y se mantiene la consistencia visual, permitiendo la sincronización con la API externa cuando la conectividad se restablezca.

---

## 🤖 Uso de IA

Este proyecto ha sido desarrollado en colaboración con **Antigravity (AI Coding Assistant)**. A continuación, se detalla la metodología aplicada:

*   **Herramientas Usadas**: Antigravity (LLM basado en Gemini 1.5 Pro).
*   **Tareas Realizadas por la IA**:
    *   Generación de la estructura base del proyecto siguiendo patrones de Arquitectura Limpia.
    *   Automatización del entorno de desarrollo mediante scripts de PowerShell para resolver problemas comunes de "cold boot" y configuración de SDK.
    *   Diseño de la interfaz estética moderna aplicando estilos avanzados de CSS-in-JS.
    *   Implementación de la navegación y el detallado de tareas con persistencia en Realm.
*   **Clave de la Supervisión Humana**:
    *   La intervención humana fue crítica para definir la experiencia de usuario (UX) deseada y el branding de "TaskNexus".
    *   Se supervisó y corrigió activamente la configuración del entorno nativo (como la detección manual del `JAVA_HOME` y el `sdk.dir`) ante errores específicos del sistema local.
    *   Se refinó la lógica de negocio para asegurar que la integración con la cámara y el sistema de archivos fuera compatible con las versiones actuales de Android.

---

## 🛠️ Tecnologías Utilizadas
*   **React Native** (v0.84)
*   **Realm** (Persistencia Local)
*   **Zustand** (Gestión de Estado)
*   **React Navigation** (Navegación Stack)
*   **Axios** (Consumo de API)
*   **Kotlin/Java** (Lógica Nativa para Cámara y Avatares)
