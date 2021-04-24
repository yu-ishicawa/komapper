package org.komapper.core

import org.komapper.core.jdbc.DataType
import java.sql.Blob
import java.sql.Clob
import java.sql.NClob
import java.sql.SQLXML
import javax.sql.DataSource

/**
 * A database.
 *
 * @property config the database configuration
 * @constructor creates a database instance
 */
class Database(val config: DatabaseConfig) {

    constructor(
        dataSource: DataSource,
        dialect: Dialect,
    ) : this(DefaultDatabaseConfig(dataSource, dialect))

    constructor(
        url: String,
        user: String = "",
        password: String = "",
        dataTypes: Set<DataType<*>> = emptySet()
    ) : this(DefaultDatabaseConfig(url, user, password, dataTypes))

    constructor(
        url: String,
        user: String = "",
        password: String = "",
        dialect: Dialect
    ) : this(DefaultDatabaseConfig(url, user, password, dialect))

    /**
     * A data type factory.
     */
    val factory = Factory(config)

    class Factory(val config: DatabaseConfig) {
        /**
         * Creates Array objects.
         *
         * @param typeName the SQL name of the type the elements of the array map to
         * @param elements the elements that populate the returned object
         */
        fun createArrayOf(typeName: String, elements: List<*>): java.sql.Array = config.session.connection.use {
            it.createArrayOf(typeName, elements.toTypedArray())
        }

        /**
         * Creates a Blob object.
         */
        fun createBlob(): Blob = config.session.connection.use {
            it.createBlob()
        }

        /**
         * Creates a Clob object.
         */
        fun createClob(): Clob = config.session.connection.use {
            it.createClob()
        }

        /**
         * Creates a NClob object.
         */
        fun createNClob(): NClob = config.session.connection.use {
            it.createNClob()
        }

        /**
         * Creates a SQLXML object.
         */
        fun createSQLXML(): SQLXML = config.session.connection.use {
            it.createSQLXML()
        }
    }
}
