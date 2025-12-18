# Kofi – Sistema POS para Cafetería

**Kofi** es una aplicación móvil Android diseñada para la gestión de pedidos en emprendimientos pequeños, como cafeterías o foodtrucks. Permite registrar órdenes de manera rápida y visual, seleccionar productos por categoría, gestionar cantidades, calcular totales automáticamente y finalizar pedidos de forma eficiente.

La aplicación está pensada para su uso en entornos reales de atención al cliente, priorizando una interfaz clara, intuitiva y moderna, que facilite el trabajo diario del personal y agilice el proceso de venta.

---

## Tecnologías utilizadas

- **Lenguaje:** Kotlin  
- **Entorno de desarrollo:** Android Studio (2025.2.1 Patch 1)  
- **Interfaz:** XML  
- **Componentes de la UI:** Activities, RecyclerView y menú hamburguesa  
- **Persistencia de datos:** Room (SQLite)  
- **Control de versiones:** GitHub  
- **Gestión del proyecto:** Trello  

---

## Guía de instalación

Antes de comenzar, asegúrate de tener instalado lo siguiente:

- Git  
- Android Studio (última versión, con SDKs necesarios)  
- Cuenta de GitHub

Esta guía describe el proceso desde la clonación del repositorio hasta la ejecución de la aplicación.

---

## Clonar el repositorio

1. Copia el enlace del repositorio desde GitHub. (https://github.com/Tutuma-jar/Kofi) 
2. Abre Android Studio.  
3. Selecciona **Get from VCS** o **Clone Repository**.  
4. Pega el enlace del repositorio en el campo **URL**.  
5. Selecciona la carpeta destino.  
6. Presiona **Clone**.  
7. Cuando aparezca el mensaje  
   *“Do you trust the authors of this project?”*, selecciona **Trust Project**.  
8. Espera la sincronización automática con Gradle.
9. Cambia la vista a Project, abre la carpeta del proyecto y copia o arrastra el archivo google-services.json dentro de la subcarpeta app.
10. Después de completar este paso, sincroniza el proyecto con Gradle para que la aplicación pueda ejecutarse correctamente.

---

### Advertencia importante

Durante la instalación y sincronización del proyecto, puede ocurrir que **Android Studio no complete correctamente la descarga de dependencias o la sincronización con Gradle**, o que dicho proceso se interrumpa sin mostrar un mensaje de error claro.

En ese caso, se recomienda:

- Aceptar las actualizaciones sugeridas de SDK o Gradle.
- Ejecutar:
  - **Build → Clean Project**
  - **Build → Rebuild Project**
- Reiniciar Android Studio si el problema persiste.

Este comportamiento es un problema conocido del IDE y no está relacionado con errores en el código del proyecto.

---

## Crear o elegir un dispositivo de prueba

### Emulador Android (AVD)

1. Ve a **Tools → Device Manager**.  
2. Crea un dispositivo virtual con **Android 8.0 o superior**.  
3. Inícialo antes de ejecutar la aplicación.

### Dispositivo físico

1. Activa el modo desarrollador y la depuración USB.  
2. Conecta el teléfono a la computadora.  
3. Acepta el permiso de conexión en el dispositivo.

---

## Ejecutar la aplicación

1. Presiona el botón **Run App** en Android Studio.  
2. Espera a que la aplicación se compile e instale.  
3. Se mostrará la pantalla principal del sistema POS.  
4. Navega por categorías y registra pedidos.

---

## Funcionalidades principales

- Gestión de productos por categoría  
- Registro de pedidos  
- Control de cantidades  
- Cálculo automático de totales  
- Persistencia local de datos  
- Modo claro y modo oscuro  

---

## Solución de errores comunes

- **Gradle sync failed:** usar *Sync Project with Gradle Files*.  
- **Error de dependencias:** verificar conexión a internet y limpiar el proyecto.  
- **La app no se instala en el emulador:** reiniciar el AVD o revisar la versión mínima del SDK.  
- **Pantalla en blanco:** verificar temas y recursos (`colors.xml` y `themes.xml`).  

---

## Autores

- Pablo Ramirez Romero  
- Natalia Arteaga Iriarte  
- Diego Loayza Arraya  
