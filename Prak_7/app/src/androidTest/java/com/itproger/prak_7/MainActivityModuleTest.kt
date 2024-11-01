package com.itproger.prak_7

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityModuleTest {
    @Test
    fun testImageIsLoaded() {
        val network = NetworkUtilities()

        val imageUrl = "https://i.pinimg.com/564x/52/ec/8a/invalid.jpg"
        val bitmapDeferred = network.downloadImage(imageUrl)

        runBlocking {
            val bitmap = bitmapDeferred.await() // Получаем загруженное изображение
            assertNotNull(bitmap) // Проверяем, что изображение не null
        }
    }
}