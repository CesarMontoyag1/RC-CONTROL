#include "BluetoothSerial.h"

BluetoothSerial SerialBT;

// ==========================
// PINES PUENTE H
// ==========================

#define IN1 5
#define IN2 18
#define IN3 19
#define IN4 21

// ==========================
// VARIABLE COMANDO
// ==========================

char comando;

// ==========================
// SETUP
// ==========================

void setup() {

  Serial.begin(115200);

  // Configurar pines
  pinMode(IN1, OUTPUT);
  pinMode(IN2, OUTPUT);
  pinMode(IN3, OUTPUT);
  pinMode(IN4, OUTPUT);

  detener();

  // Iniciar Bluetooth
  SerialBT.begin("CarroESP32");

  Serial.println("Bluetooth iniciado");
}

// ==========================
// LOOP
// ==========================

void loop() {

  if (SerialBT.available()) {

    comando = SerialBT.read();

    Serial.print("Comando recibido: ");
    Serial.println(comando);

    switch(comando) {

      case 'F':
        adelante();
        break;

      case 'B':
        atras();
        break;

      case 'L':
        izquierda();
        break;

      case 'R':
        derecha();
        break;

      case 'S':
        detener();
        break;
    }
  }
}

// ==========================
// FUNCIONES MOVIMIENTO
// ==========================

void adelante() {
  //adelante real
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);

  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);
}

void atras() {
  //Atras real
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);

  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);
}

void izquierda() {
  // Izquierda real
  digitalWrite(IN1, LOW);
  digitalWrite(IN2, HIGH);

  digitalWrite(IN3, HIGH);
  digitalWrite(IN4, LOW);
}

void derecha() {
  // derecha real  
  digitalWrite(IN1, HIGH);
  digitalWrite(IN2, LOW);

  digitalWrite(IN3, LOW);
  digitalWrite(IN4, HIGH);
}

void detener() {

  digitalWrite(IN1, LOW);
  digitalWrite(IN2, LOW);

  digitalWrite(IN3, LOW);
  digitalWrite(IN4, LOW);
}