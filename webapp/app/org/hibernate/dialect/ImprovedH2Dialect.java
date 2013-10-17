package org.hibernate.dialect;

/**
 * Work-around for a bug in Hibernate 4.2.* that causes errors when running
 * the init code against an empty database.
 * 
 * Source: https://hibernate.atlassian.net/browse/HHH-7002
 * 
 * @author Aidan Nagorcka-Smith (aidanns@gmail.com)
 */
public class ImprovedH2Dialect extends H2Dialect {

    @Override
    public String getDropSequenceString(String sequenceName) {
        // Adding the "if exists" clause to avoid warnings
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just leads to error
        // messages about missing tables when we don't have a schema in the database
        return false;
    }

}
