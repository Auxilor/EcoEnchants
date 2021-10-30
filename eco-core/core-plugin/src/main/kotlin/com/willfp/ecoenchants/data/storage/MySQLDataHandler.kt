package com.willfp.ecoenchants.data.storage

import com.willfp.ecoenchants.EcoEnchantsPlugin
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

@Suppress("UNCHECKED_CAST")
class MySQLDataHandler(
    plugin: EcoEnchantsPlugin
) : DataHandler {
    init {
        Database.connect(
            "jdbc:mysql://" +
                    "${plugin.configYml.getString("mysql.host")}:" +
                    "${plugin.configYml.getString("mysql.port")}/" +
                    plugin.configYml.getString("mysql.database"),
            driver = "com.mysql.cj.jdbc.Driver",
            user = plugin.configYml.getString("mysql.user"),
            password = plugin.configYml.getString("mysql.password")
        )

        transaction {
            Players.apply {
                /*
                Optional for auto-add fields
                 */
            }

            SchemaUtils.create(Players)
        }
    }

    override fun save() {
        // Do nothing
    }

    override fun <T> write(uuid: UUID, key: String, value: T) {
        transaction {
            Players.select { Players.id eq uuid }.firstOrNull() ?: run {
                Players.insert {
                    it[this.id] = uuid
                }
            }
            val column: Column<T> = Players.columns.stream().filter { it.name == key }.findFirst().get() as Column<T>
            Players.update({ Players.id eq uuid }) {
                it[column] = value
            }
        }
    }

    override fun <T> read(uuid: UUID, key: String): T? {
        var value: T? = null
        transaction {
            val player = Players.select { Players.id eq uuid }.firstOrNull() ?: return@transaction
            value = player[Players.columns.stream().filter { it.name == key }.findFirst().get()] as T?
        }
        return value
    }

    object Players : UUIDTable("EcoEnchants_Players") {
        override val id: Column<EntityID<UUID>> = uuid("uuid")
            .entityId()
        val descriptions = bool("descriptions")
            .default(true)
    }
}