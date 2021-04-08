package org.komapper.core.dsl.query

import org.komapper.core.DatabaseConfig
import org.komapper.core.config.Dialect
import org.komapper.core.data.Statement
import org.komapper.core.dsl.scope.TemplateUpdateOptionDeclaration
import org.komapper.core.dsl.scope.TemplateUpdateOptionScope
import org.komapper.core.jdbc.JdbcExecutor

interface TemplateExecuteQuery : Query<Int> {
    fun option(declaration: TemplateUpdateOptionDeclaration): TemplateExecuteQuery
}

internal data class TemplateExecuteQueryImpl(
    private val sql: String,
    private val params: Any = object {},
    private val option: TemplateUpdateOption = QueryOptionImpl()
) : TemplateExecuteQuery {

    override fun option(declaration: TemplateUpdateOptionDeclaration): TemplateExecuteQueryImpl {
        val scope = TemplateUpdateOptionScope(option)
        declaration(scope)
        return copy(option = scope.asOption())
    }

    override fun run(config: DatabaseConfig): Int {
        val statement = buildStatement(config.dialect)
        val executor = JdbcExecutor(config, option.asJdbcOption())
        val (count) = executor.executeUpdate(statement)
        return count
    }

    override fun dryRun(dialect: Dialect): Statement {
        return buildStatement(dialect)
    }

    private fun buildStatement(dialect: Dialect): Statement {
        val builder = dialect.templateStatementBuilder
        return builder.build(sql, params)
    }
}
