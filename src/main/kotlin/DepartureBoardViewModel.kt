import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.AuthenticationInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

private const val BASE_URL = "https://api1.raildata.org.uk/1010-live-departure-board-dep/LDBWS/api/20220120/"

private val client = OkHttpClient.Builder()
    .addInterceptor(AuthenticationInterceptor())
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    })
    .build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

@Serializable
data class ServiceLocation(
    val locationName: String,
    val crs: String,
    val via: String?
)

@Serializable
data class TrainService(
    val origin: Array<ServiceLocation>,
    val destination: Array<ServiceLocation>,
    val currentOrigins: Array<ServiceLocation>,
    val currentDestinations: Array<ServiceLocation>,
    val sta: String,
    val eta: String,
    val std: String,
    val etd: String,
    val platform: String,
    val operator: String,
    val operatorCode: String,
    val isCircularRoute: Boolean,
    val isCancelled: Boolean,
    val length: Int,
    val cancelReason: String,
    val delayReason: String
)

@Serializable
data class DepartureBoard(
    @SerialName(value = "trainServices")
    val services: Array<TrainService>,

    val generatedAt: String,
    val nrccMessages: Array<String>
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

interface OpenLDBSVWSSpec {
    @GET("GetDepBoardWithDetails/{crs}")
    suspend fun getDepBoardWithDetails(@Path("crs") crs: String): DepartureBoard
}

object OpenLDBSVWS {
    val retrofitService: OpenLDBSVWSSpec by lazy {
        retrofit.create(OpenLDBSVWSSpec::class.java)
    }
}