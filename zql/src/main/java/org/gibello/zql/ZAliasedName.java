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

import java.util.StringTokenizer;


/**
 * A name/alias association<br>
 * Names can have two forms:
 * <ul>
 * <li>FORM_TABLE for table names ([schema.]table)</li>
 * <li>FORM_COLUMN for column names ([[schema.]table.]column)</li>
 * </ul>
 */
public class ZAliasedName implements java.io.Serializable {

    String strform_ = "";
    String schema_ = null;
    String table_ = null;
    String column_ = null;
    String alias_ = null;

    public static int FORM_TABLE = 1;
    public static int FORM_COLUMN = 2;

    int form_ = FORM_COLUMN;

    public ZAliasedName() {
    }

    /**
     * Create a new ZAliasedName given it's full name.
     *
     * @param fullname The full name: [[schema.]table.]column
     * @param form     The name form (FORM_TABLE or FORM_COLUMN)
     */
    public ZAliasedName(String fullname, int form) {

        form_ = form;
        strform_ = new String(fullname);

        StringTokenizer st = new StringTokenizer(fullname, ".");
        switch (st.countTokens()) {
            case 1:
                if (form == FORM_TABLE) table_ = new String(st.nextToken());
                else column_ = new String(st.nextToken());
                break;
            case 2:
                if (form == FORM_TABLE) {
                    schema_ = new String(st.nextToken());
                    table_ = new String(st.nextToken());
                } else {
                    table_ = new String(st.nextToken());
                    column_ = new String(st.nextToken());
                }
                break;
            case 3:
            default:
                schema_ = new String(st.nextToken());
                table_ = new String(st.nextToken());
                column_ = new String(st.nextToken());
                break;
        }
        schema_ = postProcess(schema_);
        table_ = postProcess(table_);
        column_ = postProcess(column_);
    }

    private String postProcess(String val) {
        if (val == null) return null;
        if (val.indexOf("(") >= 0) val = val.substring(val.lastIndexOf("(") + 1);
        if (val.indexOf(")") >= 0) val = val.substring(0, val.indexOf(")"));
        return val.trim();
    }

    public String toString() {
        if (alias_ == null) return strform_;
        else return strform_ + " " + alias_;
    }

    /**
     * @return If the name is of the form schema.table.column,
     * returns the schema part
     */
    public String getSchema() {
        return schema_;
    }

    /**
     * @return If the name is of the form [schema.]table.column,
     * returns the schema part
     */
    public String getTable() {
        return table_;
    }

    /**
     * @return The name is of the form [[schema.]table.]column:
     * return the column part
     */
    public String getColumn() {
        return column_;
    }

    /**
     * @return true if column is "*", false otherwise.
     * Example: *, table.* are wildcards.
     */
    public boolean isWildcard() {
        if (form_ == FORM_TABLE) return table_ != null && table_.equals("*");
        else return column_ != null && column_.indexOf('*') >= 0;
    }

    /**
     * @return the alias associated to the current name.
     */
    public String getAlias() {
        return alias_;
    }

    /**
     * Associate an alias with the current name.
     *
     * @param a the alias associated to the current name.
     */
    public void setAlias(String a) {
        alias_ = new String(a);
    }
}

