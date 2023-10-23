import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.exposed.sql.Database

val database = MutableStateFlow<Database?>(null)
