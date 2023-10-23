package page.db

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import database
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import server.tables.CityTable
import server.tables.UserTable

@Composable
@Preview
fun DbDemoPage() {
    val db by database.collectAsState()

    Column {
        Button(
            onClick = {
                // 事务后会关闭链接，内存库在下次执行又是一个新的内存库。
                transaction(db) {
                    addLogger(StdOutSqlLogger)

                    SchemaUtils.create(UserTable, CityTable)

                    for (i in 0 .. 100) {
                        CityTable.insert {
                            it[id] = i.toLong()
                            it[name] = "city $i"
                        }
                    }

                    for (i in 0 .. 100) {
                        UserTable.insert {
                            it[name] = "User $i"
                            it[cityId] = i.toLong()
                        }
                    }

                    commit()
                }
            }
        ) {
            Text("执行 插入")
        }

        Button(
            onClick = {
                // 事务后会关闭链接，内存库在下次执行又是一个新的内存库。
                // 使用 文件库，数据就会被保留。
                transaction(db) {
                    // addLogger(StdOutSqlLogger)
                    (UserTable innerJoin CityTable)
                        .slice(UserTable.name, CityTable.name)
                        .select {
                            (UserTable.id.greater(UserTable.cityId) and CityTable.id.less(44)) or
                                    CityTable.id.greater(64)
                        }.forEach {
                            println("${it[UserTable.name]} => ${it[CityTable.name]}")
                        }
                }
            }
        ) {
            Text("执行 搜索")
        }
    }
}