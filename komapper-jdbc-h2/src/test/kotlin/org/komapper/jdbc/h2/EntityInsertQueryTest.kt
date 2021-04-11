package org.komapper.jdbc.h2

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.komapper.core.ClockProvider
import org.komapper.core.Database
import org.komapper.core.DatabaseConfig
import org.komapper.core.UniqueConstraintException
import org.komapper.core.dsl.EntityQuery
import org.komapper.core.dsl.execute
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@ExtendWith(Env::class)
class EntityInsertQueryTest(private val db: Database) {

    @Test
    fun test() {
        val a = Address.metamodel()
        val address = Address(16, "STREET 16", 0)
        db.execute { EntityQuery.insert(a, address) }
        val address2 = db.execute {
            EntityQuery.first(a).where {
                a.addressId eq 16
            }
        }
        assertEquals(address, address2)
    }

    @Test
    fun createdAt_localDateTime() {
        val p = Person.metamodel()
        val person1 = Person(1, "ABC")
        val person2 = db.execute { EntityQuery.insert(p, person1) }
        assertNotNull(person2.createdAt)
        assertNotNull(person2.updatedAt)
        assertEquals(person2.createdAt, person2.updatedAt)
        val person3 = db.execute {
            EntityQuery.first(p).where {
                p.personId to 1
            }
        }
        assertEquals(person2, person3)
    }

    @Test
    fun createdAt_offsetDateTime() {
        val h = Human.metamodel()
        val human1 = Human(1, "ABC")
        val human2 = db.execute { EntityQuery.insert(h, human1) }
        assertNotNull(human2.createdAt)
        assertNotNull(human2.updatedAt)
        assertEquals(human2.createdAt, human2.updatedAt)
        val human3 = db.execute {
            EntityQuery.first(h).where {
                h.humanId to 1
            }
        }
        assertEquals(human2, human3)
    }

    @Test
    fun createdAt_customize() {
        val instant = Instant.parse("2021-01-01T00:00:00Z")
        val zoneId = ZoneId.of("UTC")

        val p = Person.metamodel()
        val config = object : DatabaseConfig by db.config {
            override val clockProvider = ClockProvider {
                Clock.fixed(instant, zoneId)
            }
        }
        val myDb = Database(config)
        val person1 = Person(1, "ABC")
        val person2 = myDb.execute { EntityQuery.insert(p, person1) }
        val person3 = db.execute {
            EntityQuery.first(p).where {
                p.personId to 1
            }
        }
        assertNotNull(person2.createdAt)
        assertNotNull(person2.updatedAt)
        assertEquals(person2.createdAt, person2.updatedAt)
        assertEquals(person3, person2)
        assertEquals(LocalDateTime.ofInstant(instant, zoneId), person2.createdAt)
    }

    @Test
    fun uniqueConstraintException() {
        val a = Address.metamodel()
        val address = Address(1, "STREET 1", 0)
        assertThrows<UniqueConstraintException> {
            db.execute { EntityQuery.insert(a, address) }.let { }
        }
    }

    @Test
    fun identityGenerator() {
        for (i in 1..201) {
            val m = IdentityStrategy.metamodel()
            val strategy = IdentityStrategy(0, "test")
            val newStrategy = db.execute { EntityQuery.insert(m, strategy) }
            assertEquals(i, newStrategy.id)
        }
    }

    @Test
    fun sequenceGenerator() {
        for (i in 1..201) {
            val m = SequenceStrategy.metamodel()
            val strategy = SequenceStrategy(0, "test")
            val newStrategy = db.execute { EntityQuery.insert(m, strategy) }
            assertEquals(i, newStrategy.id)
        }
    }
}
