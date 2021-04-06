package org.komapper.core.dsl.scope

import org.komapper.core.Scope
import org.komapper.core.dsl.element.Criterion
import org.komapper.core.dsl.element.Operand
import org.komapper.core.dsl.metamodel.PropertyMetamodel

@Scope
class OnScope<ENTITY> internal constructor(
    private val context: MutableList<Criterion> = mutableListOf()
) :
    Collection<Criterion> by context {

    companion object {
        operator fun <ENTITY> OnDeclaration<ENTITY>.plus(other: OnDeclaration<ENTITY>): OnDeclaration<ENTITY> {
            return {
                this@plus(this)
                other(this)
            }
        }
    }

    infix fun <T : Any> PropertyMetamodel<*, T>.eq(right: PropertyMetamodel<ENTITY, T>) {
        context.add(Criterion.Eq(Operand.Property(this), Operand.Property(right)))
    }
}
