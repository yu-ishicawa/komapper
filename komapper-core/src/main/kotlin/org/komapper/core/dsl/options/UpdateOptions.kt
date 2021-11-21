package org.komapper.core.dsl.options

data class UpdateOptions(
    override val allowEmptyWhereClause: Boolean,
    override val batchSize: Int?,
    override val escapeSequence: String?,
    override val ignoreVersion: Boolean,
    override val queryTimeoutSeconds: Int?,
    override val suppressLogging: Boolean,
    override val suppressOptimisticLockException: Boolean
) : BatchOptions, VersionOptions, WhereOptions {

    companion object {
        val default = UpdateOptions(
            allowEmptyWhereClause = false,
            escapeSequence = null,
            batchSize = null,
            ignoreVersion = false,
            queryTimeoutSeconds = null,
            suppressLogging = false,
            suppressOptimisticLockException = false
        )
    }
}