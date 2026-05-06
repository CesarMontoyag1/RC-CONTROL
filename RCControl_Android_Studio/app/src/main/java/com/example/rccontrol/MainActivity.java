package com.example.rccontrol;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private Button btnConnect, btnForward, btnBack, btnLeft, btnRight, btnStop;
    private TextView txtStatus;

    // Listas para manejar dispositivos vinculados y nuevos
    private final List<BluetoothDevice> deviceList = new ArrayList<>();
    private boolean isReceiverRegistered = false;

    // Lanzador de permisos unificado
    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean allGranted = true;
                for (Boolean isGranted : result.values()) {
                    if (!isGranted) allGranted = false;
                }
                if (allGranted) {
                    txtStatus.setText("SISTEMA LISTO");
                } else {
                    txtStatus.setText("FALTAN PERMISOS");
                    Toast.makeText(this, "Se requieren permisos para escanear y conectar", Toast.LENGTH_LONG).show();
                }
            });

    // Receptor para encontrar dispositivos nuevos (Discovery)
    private final BroadcastReceiver discoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && !deviceList.contains(device)) {
                    // Solo lo añadimos si tiene nombre para mantener la lista limpia
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                        if (device.getName() != null) {
                            deviceList.add(device);
                            Toast.makeText(context, "Nuevo: " + device.getName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                txtStatus.setText("BÚSQUEDA FINALIZADA");
                showConnectionDialog(); // Reabrimos el diálogo con los nuevos resultados
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Coneccion bluetooth
        initUI();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth no disponible", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        checkAndRequestPermissions();

        btnConnect.setOnClickListener(v -> {
            stopDiscovery();
            showConnectionDialog();
        });

        // Configuración de controles táctiles continuos
        setupTouchControl(btnForward, "F");
        setupTouchControl(btnBack, "B");
        setupTouchControl(btnLeft, "L");
        setupTouchControl(btnRight, "R");

        // Botón STOP (Freno de emergencia)
        btnStop.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                sendCommand("S");
                v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
            }
            return true;
        });
    }

    private void initUI() {
        txtStatus = findViewById(R.id.txtStatus);
        btnConnect = findViewById(R.id.btnConnect);
        btnForward = findViewById(R.id.btnForward);
        btnBack = findViewById(R.id.btnBack);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnStop = findViewById(R.id.btnStop);
    }

    private void setupTouchControl(Button btn, String command) {
        btn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.setAlpha(0.6f);
                    sendCommand(command);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.setAlpha(1.0f);
                    sendCommand("S");
                    break;
            }
            return true;
        });
    }

    private void checkAndRequestPermissions() {
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN);
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        permissionLauncher.launch(permissions.toArray(new String[0]));
    }

    private void showConnectionDialog() {
        deviceList.clear();

        // 1. Añadir dispositivos ya vinculados
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            Set<BluetoothDevice> paired = bluetoothAdapter.getBondedDevices();
            if (paired != null) deviceList.addAll(paired);
        }

        // 2. Crear lista de nombres para el diálogo
        List<String> names = new ArrayList<>();
        for (BluetoothDevice d : deviceList) {
            String name = (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S)
                    ? d.getName() : "Unknown";
            names.add((name != null ? name : "Sin nombre") + "\n" + d.getAddress());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        builder.setTitle("SELECCIONAR DISPOSITIVO");

        builder.setItems(names.toArray(new String[0]), (dialog, which) -> {
            stopDiscovery();
            connectToDevice(deviceList.get(which));
        });

        builder.setNeutralButton("BUSCAR NUEVOS", (dialog, which) -> startDiscovery());
        builder.setNegativeButton("CERRAR", null);
        builder.show();
    }

    private void startDiscovery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            checkAndRequestPermissions();
            return;
        }

        txtStatus.setText("ESCANEANDO...");
        deviceList.clear();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoveryReceiver, filter);
        isReceiverRegistered = true;

        bluetoothAdapter.startDiscovery();
        Toast.makeText(this, "Buscando dispositivos cercanos...", Toast.LENGTH_SHORT).show();
    }

    private void stopDiscovery() {
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                bluetoothAdapter.cancelDiscovery();
            }
        }
    }

    private void connectToDevice(BluetoothDevice device) {
        txtStatus.setText("CONECTANDO...");
        executor.execute(() -> {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) return;

                if (bluetoothSocket != null) bluetoothSocket.close();

                bluetoothSocket = device.createRfcommSocketToServiceRecord(APP_UUID);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();

                runOnUiThread(() -> txtStatus.setText("EN LÍNEA: " + device.getName().toUpperCase()));

            } catch (IOException e) {
                runOnUiThread(() -> {
                    txtStatus.setText("ERROR DE VÍNCULO");
                    Toast.makeText(MainActivity.this, "No se pudo conectar", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void sendCommand(String command) {
        executor.execute(() -> {
            try {
                if (outputStream != null) {
                    outputStream.write(command.getBytes());
                    runOnUiThread(() -> {
                        // Opcional: Feedback visual de comando enviado
                    });
                }
            } catch (IOException e) {
                runOnUiThread(() -> txtStatus.setText("DESCONECTADO"));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopDiscovery();
        if (isReceiverRegistered) {
            unregisterReceiver(discoveryReceiver);
        }
        try {
            if (outputStream != null) outputStream.close();
            if (bluetoothSocket != null) bluetoothSocket.close();
        } catch (IOException ignored) {}
        executor.shutdownNow();
    }
}
