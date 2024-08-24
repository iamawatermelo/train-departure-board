import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.DepartureBoard
import net.OpenLDBWS
import retrofit2.HttpException
import java.io.IOException

sealed interface DepartureBoardState {
    data class Loaded(val board: DepartureBoard) : DepartureBoardState
    data object Error : DepartureBoardState
    data object Loading : DepartureBoardState
}

class DepartureBoardViewModel(private val crs: String) : ViewModel() {
    var currentDepartureBoardState: DepartureBoardState by mutableStateOf(DepartureBoardState.Loading)

    init {
        getDepartureBoard()
    }

    private fun getDepartureBoard() = viewModelScope.launch {
        currentDepartureBoardState = DepartureBoardState.Loading
        currentDepartureBoardState = try {
            DepartureBoardState.Loaded(OpenLDBWS.retrofitService.getDepBoardWithDetails(crs))
        } catch (e: IOException) {
            println("Exception: $e")
            DepartureBoardState.Error
        } catch (e: HttpException) {
            println("Exception: $e")
            DepartureBoardState.Error
        }
    }
}