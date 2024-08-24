import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val departureBoardViewModel = DepartureBoardViewModel("BHM")

    MaterialTheme {
        Column {
            when (val state = departureBoardViewModel.currentDepartureBoardState) {
                DepartureBoardState.Error -> Text(text = "Error!")
                DepartureBoardState.Loading -> Text(text = "Loading...")
                is DepartureBoardState.Loaded -> state.board.services.forEach {
                    Text("Arriving ${it.eta} and departing ${it.std} on Platform ${it.platform}: ${it.destination.forEach { 
                        it.locationName
                    }}")
                }
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
