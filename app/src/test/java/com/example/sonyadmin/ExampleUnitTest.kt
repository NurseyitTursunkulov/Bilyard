//package com.example.sonyadmin
//
////import com.nhaarman.mockito_kotlin.*
//import android.app.Application
//import android.content.Context
//import android.content.res.Resources
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Observer
//import com.example.sonyadmin.data.Repository
//import com.example.sonyadmin.data.Task
//import com.example.sonyadmin.data.service.UserRepository
//import com.example.sonyadmin.gameList.MyModel
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.ObsoleteCoroutinesApi
//import kotlinx.coroutines.test.TestCoroutineContext
//import net.danlew.android.joda.JodaTimeAndroid
//import org.joda.time.DateTime
//import org.junit.Assert.assertEquals
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.koin.core.KoinComponent
//import org.mockito.ArgumentMatchers.anyInt
//import org.mockito.Mockito.*
//import java.io.InputStream
//import java.util.concurrent.CountDownLatch
//import java.util.concurrent.TimeUnit
//
//
///**
// * Example local unit test, which will execute on the development machine (host).
// *
// * See [testing documentation](http://d.android.com/tools/testing).
// */
//@ObsoleteCoroutinesApi
//class ExampleUnitTest : KoinComponent {
////    val repository: Repository = com.nhaarman.mockitokotlin2.mock()
////    val application: Application = com.nhaarman.mockitokotlin2.mock()
////    val userRepository: UserRepository = com.nhaarman.mockitokotlin2.mock()
////    private val viewStateObserver: Observer<Event<String>> = com.nhaarman.mockitokotlin2.mock()
//    private lateinit var tasksViewModel: MyModel
//
//    // A CoroutineContext that can be controlled from tests
//    private val testContext = TestCoroutineContext()
//
//    // Set the main coroutines dispatcher for unit testing.
//    @ExperimentalCoroutinesApi
//    @get:Rule
//    var coroutinesMainDispatcherRule = ViewModelScopeMainDispatcherRule(testContext)
//
//    // Executes each task synchronously using Architecture Components.
//    @Rule
//    @JvmField
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    @Test
//    fun addition_isCorrect() {
//        tasksViewModel.deleteAll()
////        verify(repository).deleteAll()
//
//    }
//
//    @Test
//    fun openTaskTest() {
////        runBlocking {
//
//            val task = Task(
//                cabinId = 0,
//                isPlaying = false, summ = 4.8, id = 0, startTime = DateTime.now()
//            )
//            tasksViewModel.openTask(task)
//            assertLiveDataEventTriggered(tasksViewModel.showToast, "Mama")
////        verify(viewStateObserver).onChanged("Mama")
////        runBlocking {
////            delay(1000)
//            // Execute pending coroutines actions
////            testContext.triggerActions()
////
////            verify(repository).deleteAll()
////        }
////        }
//
//    }
//    @Test
//    fun hz(){}
//
//    @Before
//    fun setUpTaskDetailViewModel() {
//        val context = mock(Context::class.java)
//        val appContext = mock(Context::class.java)
//        val resources = mock(Resources::class.java)
//        `when`(resources.openRawResource(anyInt())).thenReturn(mock(InputStream::class.java))
//        `when`(appContext.getResources()).thenReturn(resources)
//        `when`(context.getApplicationContext()).thenReturn(appContext)
//        JodaTimeAndroid.init(context)
//
//        tasksViewModel = MyModel(repository, application, userRepository)
//        tasksViewModel.showToast.observeForever(viewStateObserver)
//    }
//
//    fun assertLiveDataEventTriggered(
//        liveData: LiveData<Event<String>>,
//        taskId: String
//    ) {
//        val value = LiveDataTestUtil.getValue(liveData)
//        assertEquals(value.getContentIfNotHandled(), taskId)
//    }
//}
//object LiveDataTestUtil {
//
//    /**
//     * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
//     * Once we got a notification via onChanged, we stop observing.
//     */
//    fun <T> getValue(liveData: LiveData<T>): T {
//        val data = arrayOfNulls<Any>(1)
//        val latch = CountDownLatch(1)
//        val observer = object : Observer<T> {
//            override fun onChanged(o: T?) {
//                data[0] = o
//                latch.countDown()
//                liveData.removeObserver(this)
//            }
//        }
//        liveData.observeForever(observer)
//        latch.await(2, TimeUnit.SECONDS)
//
//        @Suppress("UNCHECKED_CAST")
//        return data[0] as T
//    }
//}