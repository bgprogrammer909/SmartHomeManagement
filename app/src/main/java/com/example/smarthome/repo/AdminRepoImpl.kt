package com.example.smarthome.repo

import com.example.smarthome.model.AdminModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AdminRepoImpl : AdminRepo {

    private val auth = FirebaseAuth.getInstance()
    private val ref = FirebaseDatabase.getInstance().getReference("users")

    // ====== ADD USER ======
    override fun addUser(
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener

                val user = AdminModel(
                    id = uid,
                    email = email,
                    isActive = true,
                    lights = false,
                    fan = false,
                    door = false
                )

                ref.child(uid).setValue(user)
                    .addOnSuccessListener {
                        auth.signOut() // keep admin logged out of new user
                        callback(true, "User created successfully")
                    }
                    .addOnFailureListener {
                        callback(false, it.message ?: "Database error")
                    }
            }
            .addOnFailureListener {
                callback(false, it.message ?: "Auth error")
            }
    }

    // ====== FETCH ALL USERS (SAFE) ======
    override fun getAllUsers(
        callback: (Boolean, String, List<AdminModel>?) -> Unit
    ) {
        ref.get().addOnSuccessListener { snapshot ->
            val users = mutableListOf<AdminModel>()

            for (userSnap in snapshot.children) {
                val id = userSnap.key ?: continue
                val email = userSnap.child("email").getValue(String::class.java) ?: ""

                // Safe parsing for isActive
                val isActive = try {
                    val raw = userSnap.child("isActive").value
                    when (raw) {
                        is Boolean -> raw
                        is String -> raw.toBoolean()
                        else -> false
                    }
                } catch (e: Exception) {
                    false
                }

                val lights = try { userSnap.child("lights").getValue(Boolean::class.java) ?: false } catch(e: Exception){ false }
                val fan = try { userSnap.child("fan").getValue(Boolean::class.java) ?: false } catch(e: Exception){ false }
                val door = try { userSnap.child("door").getValue(Boolean::class.java) ?: false } catch(e: Exception){ false }

                users.add(AdminModel(id, email, isActive, lights, fan, door))
            }

            callback(true, "Fetched users safely", users)
        }.addOnFailureListener {
            callback(false, it.message ?: "Error fetching users", null)
        }
    }


    // ====== UPDATE USER STATUS ======
    override fun updateUserStatus(
        userId: String,
        isActive: Boolean,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId)
            .child("isActive")
            .setValue(isActive)
            .addOnSuccessListener { callback(true, "Status updated") }
            .addOnFailureListener { callback(false, it.message ?: "Status update failed") }
    }
    override fun updateUserPassword(
        userId: String,
        newPassword: String,
        callback: (Boolean, String) -> Unit
    ) {
        // You must call your Cloud Function or Admin SDK here
        // Example: call Firebase HTTPS Callable function
        val functions = com.google.firebase.functions.FirebaseFunctions.getInstance()
        val data = hashMapOf(
            "userId" to userId,
            "newPassword" to newPassword
        )

        functions.getHttpsCallable("updateUserPassword")
            .call(data)
            .addOnSuccessListener {
                callback(true, "Password updated successfully")
            }
            .addOnFailureListener { e ->
                callback(false, e.message ?: "Failed to update password")
            }
    }

    // ====== UPDATE MODULE ======
    override fun updateModule(
        userId: String,
        moduleName: String,
        moduleData: Any,
        callback: (Boolean, String) -> Unit
    ) {
        ref.child(userId)
            .child(moduleName)
            .setValue(moduleData)
            .addOnSuccessListener { callback(true, "Module updated") }
            .addOnFailureListener { callback(false, it.message ?: "Module update failed") }
    }
}
