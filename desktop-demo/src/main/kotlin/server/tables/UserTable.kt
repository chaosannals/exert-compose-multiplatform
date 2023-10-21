package server.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.Table

object UserTable : Table() {
    val id: Column<Long> = long("id").autoIncrement()
    val name: Column<String> = varchar("name", length = 50)
    val cityId: Column<Long?> = (long("city_id") references CityTable.id).nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_USER")
}