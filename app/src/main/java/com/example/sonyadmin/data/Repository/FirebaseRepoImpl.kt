package com.example.sonyadmin.data.Repository

import android.util.Log
import com.example.sonyadmin.bar.product.Product
import com.example.sonyadmin.data.DailyCount
import com.example.sonyadmin.data.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.joda.time.DateTime

class FirebaseRepoImpl : FirebaseRepository {
    private val TAG = FirebaseRepoImpl::class.java.simpleName
    var db = FirebaseFirestore.getInstance()
    override fun addBarProduct(listOfBars: ArrayList<Product>, task: Task) {
        db.collection("games").document(task.idForTitle).get().addOnSuccessListener {

            val previousTask = Gson().fromJson(
                Gson().toJson(it.data as HashMap<String, Long>), Task::class.java
            )
            Log.d(TAG, "bar :  ${previousTask.listOfBars}")

            previousTask.listOfBars.addAll(listOfBars)
            db.collection("games").document(task.idForTitle)
                .set(previousTask)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        }.addOnFailureListener {
            Log.d(TAG, "bar : ${it}")
        }
    }

    override fun setCash(dailyCount: DailyCount) {
        db.collection("cash").document("cash").get().addOnSuccessListener {
            val previousCash = Gson().fromJson(
                Gson().toJson(it.data as HashMap<String, Long>), DailyCount::class.java
            )

            db.collection("cash").document("cash").collection("history").add(previousCash).addOnSuccessListener {
                Log.d(TAG, "successfully added to history")
            }.addOnFailureListener {
                Log.d(TAG, "error adding to history")
            }
            Log.d(TAG, "get cash ${previousCash}")

        }.addOnFailureListener { e ->
            Log.w(TAG, "cash set Error adding document", e)
        }


        db.collection("cash").document("cash").set(dailyCount)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "cash written with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "cash set Error adding document", e)
            }
    }

    override fun updateCash(sTimeOfDay: DateTime, endTimeOfDay: DateTime, day: Int, sum: Double) {
        db.collection("cash").document("cash").update("summ", FieldValue.increment(sum))
    }

    override fun writeStartTime(game: Task) {
        db.collection("games").document(game.idForTitle)
            .set(game)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    override fun writeEndTime(game: Task) {
        db.collection("games").document(game.idForTitle)
            .set(game)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

        db.collection("games").document(game.idForTitle).collection("history")
            .add(game)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}