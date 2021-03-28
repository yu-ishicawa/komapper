package org.komapper.core.dsl

import org.komapper.core.dsl.query.SqlDeleteQuery
import org.komapper.core.dsl.query.SqlDeleteQueryImpl
import org.komapper.core.dsl.query.SqlInsertQuery
import org.komapper.core.dsl.query.SqlInsertQueryImpl
import org.komapper.core.dsl.query.SqlSelectQuery
import org.komapper.core.dsl.query.SqlSelectQueryImpl
import org.komapper.core.dsl.query.SqlUpdateQuery
import org.komapper.core.dsl.query.SqlUpdateQueryImpl
import org.komapper.core.metamodel.EntityMetamodel

object SqlQuery {

    fun <ENTITY> from(entityMetamodel: EntityMetamodel<ENTITY>): SqlSelectQuery<ENTITY> {
        return SqlSelectQueryImpl(entityMetamodel)
    }

    fun <ENTITY> insert(entityMetamodel: EntityMetamodel<ENTITY>): SqlInsertQuery {
        return SqlInsertQueryImpl(entityMetamodel)
    }

    fun <ENTITY> update(entityMetamodel: EntityMetamodel<ENTITY>): SqlUpdateQuery {
        return SqlUpdateQueryImpl(entityMetamodel)
    }

    fun <ENTITY> delete(entityMetamodel: EntityMetamodel<ENTITY>): SqlDeleteQuery {
        return SqlDeleteQueryImpl(entityMetamodel)
    }
}