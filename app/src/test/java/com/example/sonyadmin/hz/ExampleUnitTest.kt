package com.example.sonyadmin.hz

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.example.sonyadmin.CoroutinesTestRule
import com.example.sonyadmin.Event
import com.example.sonyadmin.LiveDataTestUtil
import com.example.sonyadmin.data.Repository
import com.example.sonyadmin.data.Result
import com.example.sonyadmin.data.Task
import com.example.sonyadmin.data.service.Api
import com.example.sonyadmin.data.service.PlaceholderPosts
import com.example.sonyadmin.gameList.MyModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.runBlockingTest
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import org.joda.time.DateTimeConstants
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.InputStream
import java.lang.Exception

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
//        tasksRepository = FakeRepo()
        tasksViewModel = MyModel(tasksRepository, applicationMock, api)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

    }

    @Test
    fun openTask_changes_livedata() = coroutinesTestRule.testDispatcher.runBlockingTest {

        tasksViewModel.openTask(
            Task(
                cabinId = 4, startTime = DateTime.now(),
                isPlaying = true, endTime = null
            )
        )
        // Execute pending coroutines actions
        testContext.triggerActions()

        assertEquals(tasksViewModel.dataLoading.value, true)
        assertLiveDataEventTriggered(tasksViewModel.showToast, " нет соединения")
    }

    @Test
    fun completeTask_makes_api_call_and_writes_to_database()= coroutinesTestRule.testDispatcher.runBlockingTest {
        doAnswer {
            Result.Success(PlaceholderPosts(4,"adf","eq",true))
        }.whenever(api).onn(any())
        val st = DateTime(2016, DateTimeConstants.MARCH, 28, 9, 10)
        val task = Task(
            cabinId = 4, startTime = st,
            isPlaying = true, endTime = null
        )
        tasksViewModel.openTask(task)
        verify(api).onn(any())
        verify(tasksRepository).writeStartTime(any())
    }

    fun assertLiveDataEventTriggered(
        liveData: LiveData<Event<String>>,
        taskId: String
    ) {
        val value = LiveDataTestUtil.getValue(liveData)
        assertEquals(value.getContentIfNotHandled(), taskId)
    }
}
