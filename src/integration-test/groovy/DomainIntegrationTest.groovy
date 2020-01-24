import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.apache.commons.lang.RandomStringUtils
import org.springframework.validation.FieldError
import spock.lang.Specification
import uniqueconstraint.Domain

@Integration
@Rollback
class DomainIntegrationTest extends Specification {

    void "setting different identifiers for child domain will not result in a validation error"() {
        given:
        Domain parent = new Domain()
        parent.is_parent = true
        Domain child1 = new Domain()
        child1.parent = parent
        Domain child2 = new Domain()
        child2.parent = parent

        and:
        String randomString = RandomStringUtils.randomAlphabetic(10)
        child1.identifier = randomString + "1"
        child1.save(flush: true)

        when:
        child2.identifier = randomString

        then:
        child2.validate()
    }

    void "setting duplicate identifier for child domain will result in a validation error"() {
        given:
        Domain parent = new Domain()
        parent.is_parent = true
        Domain child1 = new Domain()
        child1.parent = parent
        Domain child2 = new Domain()
        child2.parent = parent

        and:
        String randomString = RandomStringUtils.randomAlphabetic(10)
        child1.identifier = randomString
        child1.save(flush: true)

        when:
        child2.identifier = randomString

        then:
        !child2.validate()

        and:
        FieldError fieldError = child2.errors.getFieldError('identifier')
        fieldError.code == 'unique'
    }

    void "setting duplicate identifier for parent domain works fine"() {
        given:
        String randomString = RandomStringUtils.randomAlphabetic(10)

        and:
        Domain parent1 = new Domain()
        parent1.is_parent = true
        parent1.identifier = randomString
        parent1.save(flush: true)

        and:
        Domain parent2 = new Domain()
        parent2.is_parent = true
        parent2.save(flush: true)

        when:
        parent2.identifier = randomString

        then:
        parent2.validate()
        parent2.identifier == parent1.identifier
    }

}
