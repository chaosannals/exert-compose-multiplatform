import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database

val ioScope = CoroutineScope(Dispatchers.IO)
val mainScope = CoroutineScope(Dispatchers.Main)

val database = MutableStateFlow<Database?>(null)

val eventExceptionFlow = MutableSharedFlow<Throwable>()

fun throwFlow(t: Throwable) {
    runBlocking {
        eventExceptionFlow.emit(t)
    }
}