package net

import DepartureBoard
import OpenLDBSVWS
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.HttpException
import java.io.IOException
import java.io.Serial

sealed interface DepartureBoardState {
    data class Loaded(val board: DepartureBoard) : DepartureBoardState
    data object Error : DepartureBoardState
    data object Loading : DepartureBoardState
}

class DepartureBoardViewModel : ViewModel() {
    var currentDepartureBoardState: DepartureBoardState by mutableStateOf(DepartureBoardState.Loading)

    init {
        getDepartureBoard()
    }

    fun getDepartureBoard() = viewModelScope.launch {
        currentDepartureBoardState = DepartureBoardState.Loading
        currentDepartureBoardState = try {
            DepartureBoardState.Loaded(OpenLDBSVWS.retrofitService.getDepBoardWithDetails("CTR"))
        } catch (e: IOException) {
            println("Exception: $e")
            DepartureBoardState.Error
        } catch (e: HttpException) {
            println("Exception: $e")
            DepartureBoardState.Error
        }
    }
}