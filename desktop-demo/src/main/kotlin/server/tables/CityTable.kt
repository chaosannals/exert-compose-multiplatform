package server.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import server.tables.UserTable.autoIncrement

object CityTable: Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val name: Column<String> = varchar("name", length = 50)

    override val primaryKey = PrimaryKey(id, name="PK_CITY")
}