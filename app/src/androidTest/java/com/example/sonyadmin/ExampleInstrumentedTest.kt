package com.example.sonyadmin



import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.work.*
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.sonyadmin.util.EchoWorker
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ExampleInstrumentedTest {
    val KEY_1 = "KEY_1"
    val KEY_2 = "KEY_2"
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<MyApp>()
        val config = Configuration.Builder()
            // Set log level to Log.DEBUG to make it easier to debug
            .setMinimumLoggingLevel(Log.DEBUG)
            // Use a SynchronousExecutor here to make it easier to write tests
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = ApplicationProvider.getApplicationContext<MyApp>()
        assertEquals("com.example.sonyadmin", appContext.packageName)
    }

    @Test
    @Throws(Exception::class)
    fun testSimpleEchoWorker() {
        // Define input data
        val input = workDataOf("KEY_1" to 1, "KEY_2" to 2)

        // Create request
        val request = OneTimeWorkRequestBuilder<EchoWorker>()
            .setInputData(input)
            .build()

        val workManager = WorkManager.getInstance()
        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        workManager.enqueue(request).result.get()
        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val outputData = workInfo.outputData
        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        assertThat(outputData, `is`(input))
    }

    @Test
    @Throws(Exception::class)
    fun testEchoWorkerNoInput() {
        // Create request
        val request = OneTimeWorkRequestBuilder<EchoWorker>()
            .build()

        val workManager = WorkManager.getInstance()
        // Enqueue and wait for result. This also runs the Worker synchronously
        // because we are using a SynchronousExecutor.
        workManager.enqueue(request).result.get()
        // Get WorkInfo
        val workInfo = workManager.getWorkInfoById(request.id).get()
        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.FAILED))
    }

    @Test
    @Throws(Exception::class)
    fun testWithInitialDelay() {
        // Define input data

        val input = workDataOf(KEY_1 to 1, KEY_2 to 2)

        // Create request
        val request = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            .setInitialDelay(10, TimeUnit.MINUTES)
            .build()

        val workManager = WorkManager.getInstance()
        val testDriver = WorkManagerTestInitHelper.getTestDriver()
        // Enqueue and wait for result.
        workManager.enqueue(request).result.get()
        testDriver.setInitialDelayMet(request.id)
        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        val dailyWorkRequest = OneTimeWorkRequestBuilder<MyCoroutineWorker>()
            .setInitialDelay(15, TimeUnit.MINUTES)
            .build()
        assertThat(workInfo.state, `is`(WorkInfo.State.SUCCEEDED))
        Mockito.verify(workManager).enqueueUniqueWork("database", ExistingWorkPolicy.KEEP, dailyWorkRequest);
    }

    @Test
    @Throws(Exception::class)
    fun testPeriodicWork() {
        // Define input data
        val input = workDataOf(KEY_1 to 1, KEY_2 to 2)

        // Create request
        val request = PeriodicWorkRequestBuilder<EchoWorker>(15, TimeUnit.MINUTES)
            .setInputData(input)
            .build()

        val workManager = WorkManager.getInstance()
        val testDriver = WorkManagerTestInitHelper.getTestDriver()
        // Enqueue and wait for result.
        workManager.enqueue(request).result.get()
        // Tells the testing framework the period delay is met
        testDriver.setPeriodDelayMet(request.id)
        // Get WorkInfo and outputData
        val workInfo = workManager.getWorkInfoById(request.id).get()
        // Assert
        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))
    }

}
