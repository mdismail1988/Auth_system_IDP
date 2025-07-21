package org.ismail.authgateway.data

data class AuthAttempt(
    val id: String,
    val email: String,
    val deviceName : String,
    val location: String,
    val ipAddress: String,
    val timestamp: Long = System.currentTimeMillis(),
    val successful: Boolean,
    var status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
) {
    override fun toString(): String {
        return "AuthAttempt(id=$id, email='$email', ipAddress='$ipAddress', timestamp=$timestamp, successful=$successful)"
    }
}