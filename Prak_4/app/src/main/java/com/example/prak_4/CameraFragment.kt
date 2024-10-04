package com.example.prak_4

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.prak_4.databinding.FragmentCameraBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding

    // Переменная, которая хранит ExecutorService для запуска фоновых задач. В данном случае используется для запуска камеры в отдельном потоке
    private lateinit var cameraExecutor: ExecutorService

    // Виджет, на котором будет отображаться видео с камеры
    private lateinit var viewFinder: PreviewView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                startCamera()
            } else {
                requireActivity().finish()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        binding.btnPht.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCamera_to_fragmentPhotos)
        }

        // Создание потока для выполнения фоновых задач, связанных с камерой
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder = binding.previewView
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }

        binding.btnTake.setOnClickListener {
            saveDateTimeToFile()
        }
        return binding.root
    }

    private fun startCamera() {
        // Получение экземпляра ProcessCameraProvider, который управляет привязкой камеры к жизненному циклу активности
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        // Асинхронно слушает результат будущего объекта cameraProviderFuture.
        // Когда результат доступен, вызывается лямбда-выражение, где в cameraProvider возвращается экземпляр ProcessCameraProvider.
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            // Выбор камеры
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Отключает все предыдущие привязки камеры (если они были).
                cameraProvider.unbindAll()

                // Привязывает камеру и ее предварительный просмотр (preview) к жизненному циклу активности.
                // Камера будет автоматически запускаться и останавливаться при изменении состояния активности.
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    // Метод проверяет, были ли предоставлены все необходимые разрешения.
    // Он использует функцию all, которая проходит по массиву REQUIRED_PERMISSIONS и проверяет для каждого
    // элемента с помощью ContextCompat.checkSelfPermission,было ли предоставлено разрешение.
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun saveDateTimeToFile() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        // текущая дата с указанным выше форматом
        val currentDateTime = dateFormat.format(Date())

        val photosDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photos")
        if (!photosDir.exists()) {
            photosDir.mkdirs()
        }

        // запись в файл
        val dateFile = File(photosDir, "date.txt")
        try {
            FileOutputStream(dateFile, true).use { outputStream ->
                outputStream.write("$currentDateTime\n".toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // завершает работу cameraExecutor
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    // Это объект, который существует в единственном экземпляре и доступен через класс.
    // Он используется для хранения констант:
    // REQUEST_CODE_PERMISSIONS: Константа для идентификации запроса разрешений.
    // REQUIRED_PERMISSIONS: Массив необходимых разрешений (в данном случае, разрешение на использование камеры).
    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}