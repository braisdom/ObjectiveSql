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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * ZUpdate: an SQL UPDATE statement.
 */
public class ZUpdate implements ZStatement {

    String table_;
    String alias_ = null;
    Hashtable set_;
    ZExp where_ = null;
    Vector columns_ = null;

    /**
     * Create an UPDATE statement on a given table.
     */
    public ZUpdate(String tab) {
        table_ = new String(tab);
    }

    public String getTable() {
        return table_;
    }

    public void setAlias(String alias) {
        alias_ = alias;
    }

    public String getAlias() {
        return alias_;
    }

    /**
     * Insert a SET... clause in the UPDATE statement
     *
     * @param t A Hashtable, where keys are column names (the columns to update),
     *          and values are ZExp objects (the column values).
     *          For example, the values may be ZConstant objects (like "Smith") or
     *          more complex SQL Expressions.
     */
    public void addSet(Hashtable t) {
        set_ = t;
    }

    /**
     * Get the whole SET... clause
     *
     * @return A Hashtable, where keys are column names (the columns to update),
     * and values are ZExp objects (Expressions that specify column values: for
     * example, ZConstant objects like "Smith").
     */
    public Hashtable getSet() {
        return set_;
    }

    /**
     * Add one column=value pair to the SET... clause
     * This method also keeps track of the column order
     *
     * @param col The column name
     * @param val The column value
     */
    public void addColumnUpdate(String col, ZExp val) {
        if (set_ == null) set_ = new Hashtable();
        set_.put(col, val);
        if (columns_ == null) columns_ = new Vector();
        columns_.addElement(col);
    }

    /**
     * Get the SQL expression that specifies a given column's update value.
     * (for example, a ZConstant object like "Smith").
     *
     * @param col The column name.
     * @return a ZExp, like a ZConstant representing a value, or a more complex
     * SQL expression.
     */
    public ZExp getColumnUpdate(String col) {
        return (ZExp) set_.get(col);
    }

    /**
     * Get the SQL expression that specifies a given column's update value.
     * (for example, a ZConstant object like "Smith").<br>
     * WARNING: This method will work only if column/value pairs have been
     * inserted using addColumnUpdate() - otherwise it is not possible to guess
     * what the right order is, and null will be returned.
     *
     * @param num The column index (starting from 1).
     * @return a ZExp, like a ZConstant representing a value, or a more complex
     * SQL expression.
     */
    public ZExp getColumnUpdate(int index) {
        if (--index < 0) return null;
        if (columns_ == null || index >= columns_.size()) return null;
        String col = (String) columns_.elementAt(index);
        return (ZExp) set_.get(col);
    }

    /**
     * Get the column name that corresponds to a given index.<br>
     * WARNING: This method will work only if column/value pairs have been
     * inserted using addColumnUpdate() - otherwise it is not possible to guess
     * what the right order is, and null will be returned.
     *
     * @param num The column index (starting from 1).
     * @return The corresponding column name.
     */
    public String getColumnUpdateName(int index) {
        if (--index < 0) return null;
        if (columns_ == null || index >= columns_.size()) return null;
        return (String) columns_.elementAt(index);
    }

    /**
     * Returns the number of column/value pairs in the SET... clause.
     */
    public int getColumnUpdateCount() {
        if (set_ == null) return 0;
        return set_.size();
    }

    /**
     * Insert a WHERE... clause in the UPDATE statement
     *
     * @param w An SQL Expression compatible with a WHERE... clause.
     */
    public void addWhere(ZExp w) {
        where_ = w;
    }

    /**
     * Get the WHERE clause of this UPDATE statement.
     *
     * @return An SQL Expression compatible with a WHERE... clause.
     */
    public ZExp getWhere() {
        return where_;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("update " + table_);
        if (alias_ != null) buf.append(" " + alias_);
        buf.append(" set ");

        Enumeration e;
        if (columns_ != null) e = columns_.elements();
        else e = set_.keys();
        boolean first = true;
        while (e.hasMoreElements()) {
            String key = e.nextElement().toString();
            if (!first) buf.append(", ");
            buf.append(key + "=" + set_.get(key).toString());
            first = false;
        }

        if (where_ != null) buf.append(" where " + where_.toString());
        return buf.toString();
    }
};

