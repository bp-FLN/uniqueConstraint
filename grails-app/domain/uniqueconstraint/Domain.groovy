package uniqueconstraint

class Domain {

    String identifier
    boolean is_parent
    Domain parent

    static constraints = {
        identifier(nullable: true, blank: true, unique: 'parent')
        parent(nullable: true)
    }
}
