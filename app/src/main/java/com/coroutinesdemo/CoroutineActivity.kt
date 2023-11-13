package com.coroutinesdemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class CoroutineActivity : AppCompatActivity() {

    private val TAG = "CoroutineActivity"

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)


        /*
        simple launch the coroutine
        */
        /*GlobalScope.launch {
            delay(5000L)
            Log.d(TAG, "Coroutine says hello from thread ${Thread.currentThread().name}")
        }
        Log.d(TAG, "Hello from thread ${Thread.currentThread().name}")*/




        /*
        launch the coroutine with suspend function
        */
        /*GlobalScope.launch {
            val networkCallAnswer1 = doNetworkCall1()
            val networkCallAnswer2 = doNetworkCall2()
            Log.d(TAG, networkCallAnswer1)
            Log.d(TAG, networkCallAnswer2)
        }*/




        /*
        switch the context of coroutine using withContext
        */
        /*GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "Starting coroutine in thread ${Thread.currentThread().name}")
            val answer = doNetworkCall1()
            withContext(Dispatchers.Main){
                Log.d(TAG, "Setting text in thread ${Thread.currentThread().name}")
                val tvDemo = findViewById<TextView>(R.id.tv_demo_text)
                tvDemo.text = answer
            }
        }*/




        /*
        use of runBlock
        */
        /*Log.d(TAG, "Before the runBlocking")
        runBlocking {
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG, "Finished IO coroutine 1")
            }
            launch(Dispatchers.IO) {
                delay(3000L)
                Log.d(TAG, "Finished IO coroutine 2")
            }
            Log.d(TAG, "Starting of runBlocking")
            delay(5000L)
            Log.d(TAG, "End of runBlocking")
        }
        Log.d(TAG, "After runBlocking")*/




        /*
        jobs, cancelation, waiting
        */

        val job = GlobalScope.launch(Dispatchers.Default) {
            /*repeat(5){
                Log.d(TAG, "Coroutine is still working...")
                delay(1000L)
            }*/
            Log.d(TAG, "Start long running calculation...")
            for (i in 30..40){
                // for checking coroutine is active or not
                if (isActive){
                    Log.d(TAG, "Result for i = $i: ${fib(i)}")
                }

            }

            // its use to cancel the coroutine if calculation taking much time as per the mentioned in timeout.
            withTimeout(3000L){
                for (i in 30..40){
                    // for checking coroutine is active or not
                    if (isActive){
                        Log.d(TAG, "Result for i = $i: ${fib(i)}")
                    }

                }
            }
            Log.d(TAG, "Ending long running calculation...")
        }

        // if we are using withTimeout function to cancel the job then no need to use runBlocking for cancel the job.
        /*runBlocking {
            //job.join()
            //Log.d(TAG, "Main Thread is continuing...")

            delay(2000L)
            job.cancel()
            Log.d(TAG, "Cancelled Job!")
        }*/




        /*
        Async and await
        */

       /* GlobalScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
               //val answer1 = networkCall1()
               //val answer2 = networkCall2()
                val answer1 = async { networkCall1() }
                val answer2 = async { networkCall2()}
                Log.d(TAG, "Answer1 is ${answer1.await()}")
                Log.d(TAG, "Answer2 is ${answer2.await()}")
            }
            Log.d(TAG, "Request took $time ms.")
        }*/




        /*
        lifecycleScope and viewModelScope - Kotlin Coroutines
        */
        val tvDemo = findViewById<TextView>(R.id.tv_demo_text)
        tvDemo.setOnClickListener {
            lifecycleScope.launch {
                while (true){
                    delay(1000L)
                    Log.d(TAG, "Still running...")
                }
            }

            GlobalScope.launch {
                delay(5000L)
                Intent(this@CoroutineActivity, SecondActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }

    }

    suspend fun networkCall1(): String{
        return "Answer1"
    }
    suspend fun networkCall2(): String{
        return "Answer2"
    }

    fun fib(n: Int): Long{
        return when (n) {
            0 -> 0
            1 -> 1
            else -> fib(n-1 ) + fib(n-2)
        }
    }

    suspend fun doNetworkCall1(): String{
        delay(3000L)
        return "This is the answer1"
    }

    suspend fun doNetworkCall2(): String{
        delay(1000L)
        return "This is the answer2"
    }
}