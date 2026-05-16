# RC-CONTROL 🏎️📱

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-brightgreen?style=for-the-badge&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Hardware-ESP32-blue?style=for-the-badge&logo=espressif&logoColor=white"/>
  <img src="https://img.shields.io/badge/Firmware-Arduino-00979D?style=for-the-badge&logo=arduino&logoColor=white"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge"/>
</p>

<p align="center">
  <b>RC-CONTROL</b> es un proyecto de control remoto para un carro RC desarrollado con una aplicación Android
  y un sistema de control basado en <b>ESP32 / Arduino</b>, diseñado para enviar comandos por Bluetooth
  y controlar el movimiento del vehículo en tiempo real.
</p>

---

## 🚗 Montaje físico

<p align="center">
  <img src="https://github.com/user-attachments/assets/f30bf9cf-06fb-43c4-9fd7-2e6c44f27008" width="700"/>
</p>

---

## 📱 Aplicación Android


<table align="center" border="0" cellspacing="0" cellpadding="12">
  <tr>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/a65054bd-f41a-4299-aaf6-0e940c623880" width="170"/>
      <br/><sub>Inicio</sub>
    </td>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/4f7b7e1b-a7b9-4963-b3e5-8bcebe5ad564" width="340"/>
      <br/><sub>Vista horizontal</sub>
    </td>
    <td align="center" valign="middle">
      <img src="https://github.com/user-attachments/assets/226a25f2-ce52-415b-a86d-bb508d98686b" width="170"/>
      <br/><sub>Vista vertical</sub>
    </td>
  </tr>
</table>

---

## 🔌 Cableado y conexiones

<p align="center">
  <img src="https://github.com/user-attachments/assets/aa266cea-a8dd-42e6-8d36-da7f03e965f6" width="800"/>
</p>

---

## 📥 Descarga el APK

Para instalar la aplicación directamente en tu dispositivo Android sin necesidad de compilar el código:

1. Ve a la sección de **[Releases](https://github.com/CesarMontoyag1/RC-CONTROL/releases)** del repositorio.
2. Busca la versión más reciente (ej. `v1.0`).
3. En el apartado **Assets**, descarga el archivo `.apk` (ej. `app-release.apk`).
4. Abre el archivo en tu celular y acepta la instalación de **"Fuentes desconocidas"** si el sistema lo solicita.

---

## ✨ Características principales

| Característica | Descripción |
|---|---|
| 📡 **Conectividad Bluetooth** | Emparejamiento rápido con módulos HC-05, HC-06 o similares |
| 🎮 **Interfaz intuitiva** | Botones de control direccional optimizados para respuesta rápida |
| ⚡ **Baja latencia** | Envío de comandos en tiempo real para control preciso |
| 📲 **Compatibilidad** | Compatible con una amplia gama de versiones de Android |

---

## 📝 Comandos enviados

La aplicación envía caracteres simples por el puerto serie que el **ESP32** interpreta para controlar el vehículo:

<p align="center">

| Tecla | Comando | Acción |
|:---:|:---:|:---:|
| `F` | Forward | ⬆️ Avanzar |
| `B` | Back | ⬇️ Retroceder |
| `L` | Left | ⬅️ Izquierda |
| `R` | Right | ➡️ Derecha |
| `S` | Stop | ⏹️ Detener |

</p>

---

## 📖 Sobre el proyecto

Este proyecto fue realizado para controlar un carro RC mediante comunicación inalámbrica por Bluetooth.  
La app Android envía comandos simples al **ESP32**, y el microcontrolador interpreta esas instrucciones para activar el **puente H**, que a su vez controla los motores DC reductores del vehículo.

El carro fue ensamblado con los siguientes componentes:

| Componente | Descripción |
|---|---|
| 🧠 **ESP32** | Controlador principal |
| ⚡ **Puente H** | Manejo de dirección de motores |
| 🔋 **2× Batería 3.7V** | Alimentación del sistema |
| ⚙️ **Motores DC reductores** | Propulsión del vehículo |
| 🛞 **Ruedas de tracción** | Desplazamiento |
| 🏗️ **Chasis RC** | Estructura física del carro |

---

## 🛠️ Tecnologías utilizadas

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"/>
  <img src="https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white"/>
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"/>
  <img src="https://img.shields.io/badge/Bluetooth-0082FC?style=for-the-badge&logo=bluetooth&logoColor=white"/>
</p>

---

## 🔧 Configuración para desarrolladores

Si deseas modificar el código o añadir nuevas funciones:

1. **Clonar el repositorio:**
```bash
   git clone https://github.com/CesarMontoyag1/RC-CONTROL.git
```

2. **Abrir en Android Studio:**
   - Importa el proyecto desde la carpeta `RCControl_Android_Studio/`.
   - Espera a que Gradle sincronice todas las dependencias.

3. **Ejecutar:**
   - Conecta tu teléfono en modo **depuración USB** o usa un emulador con soporte Bluetooth.

---

## 📁 Estructura del repositorio

```bash
RC-CONTROL/
├── RCControl_Android_Studio/   # Proyecto de la aplicación Android
├── arduino/                    # Firmware para el ESP32 / Arduino
│   └── RCCONTROL.ino
└── README.md
```

---

## 👤 Autor

<p align="center">
  <b>Cesar Montoya</b> — Desarrollador<br/>
  <a href="https://github.com/CesarMontoyag1">@CesarMontoyag1</a>
</p>

---

## 📄 Licencia

Este proyecto está bajo la **Licencia MIT** — mira el archivo [LICENSE.md](LICENSE.md) para más detalles.  
Siéntete libre de usarlo para fines educativos.

---

<p align="center">
  Si te gusta este proyecto, ¡no olvides darle una ⭐ en GitHub!
</p>
