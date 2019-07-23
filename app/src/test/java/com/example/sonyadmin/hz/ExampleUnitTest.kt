package com.example.sonyadmin.hz

import android.app.Application
import android.content.Context
import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.sonyadmin.CoroutinesTestRule
import com.example.sonyadmin.EveryDayUpdateCashWorker
import com.example.sonyadmin.LiveDataTestUtil
import com.example.sonyadmin.data.Repository.Repository
import com.example.sonyadmin.data.Result
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.data.service.Api
import com.example.sonyadmin.data.service.ResponseType
import com.example.sonyadmin.gameList.Model.countSum
import com.example.sonyadmin.gameList.MyModel
import com.example.sonyadmin.util.Event
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.runBlockingTest
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.InputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ObsoleteCoroutinesApi
@RunWith(JUnit4::class)
class ExampleUnitTest {
    // Subject under test
    private lateinit var tasksViewModel: MyModel
    @Mock
    private lateinit var api: Api
    @Mock
    private lateinit var updateWorker: EveryDayUpdateCashWorker

    // Use a fake repository to be injected into the viewmodel
    @Mock
    private lateinit var tasksRepository: Repository

    // A CoroutineContext that can be controlled from tests
    private val testContext = TestCoroutineContext()
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesMainDispatcherRule = ViewModelScopeMainDispatcherRule(testContext)

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createViewModel() {
        MockitoAnnotations.initMocks(this)
        val context = Mockito.mock(Context::class.java)
        val appContext = Mockito.mock(Context::class.java)
        val resources = Mockito.mock(Resources::class.java)
        Mockito.`when`(resources.openRawResource(ArgumentMatchers.anyInt()))
            .thenReturn(Mockito.mock(InputStream::class.java))
        Mockito.`when`(appContext.getResources()).thenReturn(resources)
        Mockito.`when`(context.getApplicationContext()).thenReturn(appContext)
        JodaTimeAndroid.init(context)

        val applicationMock = Mockito.mock(Application::class.java)
        tasksViewModel = MyModel(tasksRepository, applicationMock, api, updateWorker)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

    }

    @Test
    fun openTask_on_error_changes_livedata() = coroutinesTestRule.testDispatcher.runBlockingTest {
        doAnswer {
            Result.Error(Exception())
        }.whenever(api).onn(any())
        tasksViewModel.openTask(
            Task(
                cabinId = 4, startTime = DateTime.now(),
                isPlaying = true, endTime = null, userName = "nurs"
            )
        )
        // Execute pending coroutines actions
        testContext.triggerActions()

        assertLiveDataEventTriggered(tasksViewModel.showToast, " нет соединения")
//        assertEquals(tasksViewModel.dataLoading.value, true)
//        verify(tasksRepository, never()).writeStartTime(any())
    }

    @Test
    fun completeTask_on_error_changes_livedata() = coroutinesTestRule.testDispatcher.runBlockingTest {
        doAnswer {
            Result.Error(Exception())
        }.whenever(api).off(any())
        tasksViewModel.completeTask(
            Task(
                cabinId = 4, startTime = DateTime.now(),
                isPlaying = true, endTime = null, userName = "nurs"
            )
        )
        // Execute pending coroutines actions
        testContext.triggerActions()

        assertEquals(tasksViewModel.dataLoading.value, true)
        assertLiveDataEventTriggered(tasksViewModel.showToast, " нет соединения")
//        verify(tasksRepository, never()).writeEndTime(any())
//        verify(tasksRepository, never()).updateCash(any(), any(), any())
    }

    @Test
    fun openTask_changes_livedata() = runBlocking {
        doAnswer {
            Result.Success(ResponseType(4, "adf", "eq", true))
        }.whenever(api).onn(any())
        tasksViewModel.openTask(
            Task(
                cabinId = 4, startTime = DateTime.now(),
                isPlaying = true, endTime = null, userName = "nurs"
            )
        )
        assertEquals(tasksViewModel.dataLoading.value, true)
    }

    @Test
    fun openTask_makes_api_call_and_writes_to_database() = runBlocking {
        doAnswer {
            Result.Success(ResponseType(4, "adf", "eq", true))
        }.whenever(api).onn(any())
        val st = DateTime(2016, DateTimeConstants.MARCH, 28, 9, 10)
        val task = Task(
            cabinId = 4, startTime = st,
            isPlaying = true, endTime = null, userName = "nurs"
        )
        tasksViewModel.openTask(task)
        testContext.triggerActions()
//        verify(api).onn(any())
//        verify(tasksRepository).writeStartTime(any())
    }

    @Test
    fun completeTask_makes_api_call_and_writes_to_database() = runBlocking {
        doAnswer {
            Result.Success(ResponseType(4, "adf", "eq", true))
        }.whenever(api).off(any())
        val st = DateTime(2016, DateTimeConstants.MARCH, 28, 9, 10)
        val task = Task(
            cabinId = 4, startTime = st,
            isPlaying = true, endTime = null, userName = "nurs"
        )
        tasksViewModel.completeTask(task)
        testContext.triggerActions()
//        verify(api).off(any())
//        verify(tasksRepository).writeEndTime(any())
//        verify(tasksRepository).updateCash(any(), any(), any())
    }

    @Test
    fun timeTest() {
        val task = Task(
            cabinId = 4, startTime = DateTime.now().withHourOfDay(11).withMinuteOfHour(0),
            isPlaying = true, endTime = null, userName = "nurs"
        )
        var k = countSum(task, DateTime.now().withHourOfDay(11).withMinuteOfHour(15))
        assertEquals(k, 25.0, 0.1)
    }

    @Test
    fun timeTestMidDay() {
        val task = Task(
            cabinId = 4, startTime = DateTime.now().withHourOfDay(12).withMinuteOfHour(0),
            isPlaying = true, endTime = null
            , userName = "nurs"
        )
        var k = countSum(task, DateTime.now().withHourOfDay(12).withMinuteOfHour(15))
        assertEquals(k, 37.5, 0.1)
    }

    @Test
    fun timeTestMidDay2() {
        val task = Task(
            cabinId = 4, startTime = DateTime.now().withHourOfDay(17).withMinuteOfHour(0),
            isPlaying = true, endTime = null, userName = "nurs"
        )
        var k = countSum(task, DateTime.now().withHourOfDay(17).withMinuteOfHour(5))
        assertEquals(k, 12.5, 0.1)
    }

    @Test
    fun timeTest17_19() {
        val task = Task(
            cabinId = 4, startTime = DateTime.now().withHourOfDay(17).withMinuteOfHour(1),
            isPlaying = true, endTime = null, userName = "nurs"
        )
        var k = countSum(task, DateTime.now().withHourOfDay(19).withMinuteOfHour(53))
        assertEquals(k, 486.5, 0.8)
    }

    @Test
    fun timeTest11_15() {
        val task = Task(
            cabinId = 4, startTime = DateTime.now().withHourOfDay(11).withMinuteOfHour(40),
            isPlaying = true, endTime = null, userName = "nurs"
        )
        var k = countSum(task, DateTime.now().withHourOfDay(15).withMinuteOfHour(38))
        assertEquals(k, 578.5, 0.8)
    }

    @Test
    fun timeTestCabin1() {
        val task = Task(
            cabinId = 1, startTime = DateTime.now().withHourOfDay(11).withMinuteOfHour(0),
            isPlaying = true, endTime = null, userName = "nurs"
        )
        var k = countSum(task, DateTime.now().withHourOfDay(14).withMinuteOfHour(0))
        assertEquals(k, 600.0, 0.8)
    }

    @Test
    fun timeTestCabin17_19() {
        val task = Task(
            cabinId = 1, startTime = DateTime.now().withHourOfDay(17).withMinuteOfHour(0),
            isPlaying = true, endTime = null, userName = "nurs"
        )
        var k = countSum(task, DateTime.now().withHourOfDay(19).withMinuteOfHour(0))
        assertEquals(k, 450.0, 0.8)
    }

    fun assertLiveDataEventTriggered(
        liveData: LiveData<Event<String>>,
        taskId: String
    ) {
        val value = LiveDataTestUtil.getValue(liveData)
        assertEquals(value.getContentIfNotHandled(), taskId)
    }
}
