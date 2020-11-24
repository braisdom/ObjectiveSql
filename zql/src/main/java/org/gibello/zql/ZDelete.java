/*
 * This file is part of Zql.
 *
 * Zql is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Zql is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Zql.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gibello.zql;

/**
 * ZDelete: an SQL DELETE statement.<br>
 * SQL Syntax: DELETE [from] table [where Expression];
 */
public class ZDelete implements ZStatement {

    String table_;
    ZExp where_ = null;

    /**
     * Create a DELETE statement on a given table
     *
     * @param tab the table name
     */
    public ZDelete(String tab) {
        table_ = new String(tab);
    }

    /**
     * Add a WHERE clause to the DELETE statement
     *
     * @param w An SQL expression compatible with a WHERE clause
     */
    public void addWhere(ZExp w) {
        where_ = w;
    }

    /**
     * @return The table concerned by the DELETE statement.
     */
    public String getTable() {
        return table_;
    }

    /**
     * @return The SQL Where clause of the DELETE statement (an SQL Expression
     * or Subquery, compatible with an SQL WHERE clause).
     */
    public ZExp getWhere() {
        return where_;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("delete ");
        if (where_ != null) buf.append("from ");
        buf.append(table_);
        if (where_ != null) buf.append(" where " + where_.toString());
        return buf.toString();
    }
};

