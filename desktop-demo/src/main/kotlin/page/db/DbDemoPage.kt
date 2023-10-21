package page.db

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import server.tables.CityTable
import server.tables.UserTable

@Composable
@Preview
fun DbDemoPage() {
    Column {
        Button(
            onClick = {
                transaction {
                    addLogger(StdOutSqlLogger)

                    SchemaUtils.create(UserTable, CityTable)

                    CityTable.insert {
                        it[name] = "city 1"
                    }

                    UserTable.insert {
                        it[name] = "111"
                        it[cityId] = 1
                    }
                }
            }
        ) {
            Text("执行1")
        }
    }
}