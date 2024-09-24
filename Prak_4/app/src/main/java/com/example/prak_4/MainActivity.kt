package com.example.prak_4

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    // Переменная, которая хранит ExecutorService для запуска фоновых задач. В данном случае используется для запуска камеры в отдельном потоке
    private lateinit var cameraExecutor: ExecutorService

    // Виджет, на котором будет отображаться видео с камеры
    private lateinit var viewFinder: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Создание потока для выполнения фоновых задач, связанных с камерой
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder = findViewById(R.id.previewView)

        // Проверка разрешений
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun startCamera() {
        // Получение экземпляра ProcessCameraProvider, который управляет привязкой камеры к жизненному циклу активности
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(this)

        // Асинхронно слушает результат будущего объекта cameraProviderFuture.
        // Когда результат доступен, вызывается лямбда-выражение, где в cameraProvider возвращается экземпляр ProcessCameraProvider.
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider =
                cameraProviderFuture.get()

            // Создается объект Preview, который предоставляет поток с камеры для отображения на экране.
            // Метод setSurfaceProvider() привязывает Preview к PreviewView.
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            // Выбор камеры
            val cameraSelector =
                CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Отключает все предыдущие привязки камеры (если они были).
                cameraProvider.unbindAll()

                // Привязывает камеру и ее предварительный просмотр (preview) к жизненному циклу активности.
                // Камера будет автоматически запускаться и останавливаться при изменении состояния активности.
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector, preview
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    // Метод проверяет, были ли предоставлены все необходимые разрешения.
    // Он использует функцию all, которая проходит по массиву REQUIRED_PERMISSIONS и проверяет для каждого
    // элемента с помощью ContextCompat.checkSelfPermission,было ли предоставлено разрешение.
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) ==
                PackageManager.PERMISSION_GRANTED
    }

    // Этот метод вызывается после того, как пользователь ответит на запрос разрешений.
    // Он проверяет, что запрос разрешений был инициирован с нужным requestCode и если все разрешения были предоставлены, снова запускает камеру.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode, permissions,
            grantResults
        )
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // Это объект, который существует в единственном экземпляре и доступен через класс.
    // Он используется для хранения констант:
    // REQUEST_CODE_PERMISSIONS: Константа для идентификации запроса разрешений.
    // REQUIRED_PERMISSIONS: Массив необходимых разрешений (в данном случае, разрешение на использование камеры).
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA)
    }
}