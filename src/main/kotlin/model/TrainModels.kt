package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceLocation(
    val locationName: String,
    val crs: String,
    val via: String? = null
)

@Serializable
data class TrainService(
    val destination: Array<ServiceLocation>,
    val currentDestinations: Array<ServiceLocation>? = null,
    val sta: String? = null,
    val eta: String? = null,
    val std: String? = null,
    val etd: String? = null,
    val platform: String? = null,
    val operator: String,
    val operatorCode: String,
    val isCircularRoute: Boolean = false,
    val isCancelled: Boolean,
    val length: Int = 0,
    val cancelReason: String? = null,
    val delayReason: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TrainService

        if (!destination.contentEquals(other.destination)) return false
        if (currentDestinations != null) {
            if (other.currentDestinations == null) return false
            if (!currentDestinations.contentEquals(other.currentDestinations)) return false
        } else if (other.currentDestinations != null) return false
        if (sta != other.sta) return false
        if (eta != other.eta) return false
        if (std != other.std) return false
        if (etd != other.etd) return false
        if (platform != other.platform) return false
        if (operator != other.operator) return false
        if (operatorCode != other.operatorCode) return false
        if (isCircularRoute != other.isCircularRoute) return false
        if (isCancelled != other.isCancelled) return false
        if (length != other.length) return false
        if (cancelReason != other.cancelReason) return false
        if (delayReason != other.delayReason) return false

        return true
    }

    override fun hashCode(): Int {
        var result = destination.contentHashCode()
        result = 31 * result + (currentDestinations?.contentHashCode() ?: 0)
        result = 31 * result + (sta?.hashCode() ?: 0)
        result = 31 * result + (eta?.hashCode() ?: 0)
        result = 31 * result + (std?.hashCode() ?: 0)
        result = 31 * result + (etd?.hashCode() ?: 0)
        result = 31 * result + (platform?.hashCode() ?: 0)
        result = 31 * result + operator.hashCode()
        result = 31 * result + operatorCode.hashCode()
        result = 31 * result + isCircularRoute.hashCode()
        result = 31 * result + isCancelled.hashCode()
        result = 31 * result + length
        result = 31 * result + (cancelReason?.hashCode() ?: 0)
        result = 31 * result + (delayReason?.hashCode() ?: 0)
        return result
    }
}

@Serializable
data class DepartureBoard(
    @SerialName(value = "trainServices")
    val services: Array<TrainService> = emptyArray(),

    val generatedAt: String,
    val nrccMessages: Array<String>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DepartureBoard

        if (!services.contentEquals(other.services)) return false
        if (generatedAt != other.generatedAt) return false
        if (!nrccMessages.contentEquals(other.nrccMessages)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = services.contentHashCode()
        result = 31 * result + generatedAt.hashCode()
        result = 31 * result + nrccMessages.contentHashCode()
        return result
    }
}