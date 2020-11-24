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
 * ZTransactStmt: an SQL statement that concerns database transactions
 * (example: COMMIT, ROLLBACK, SET TRANSACTION)
 */
public class ZTransactStmt implements ZStatement {

    String statement_;
    String comment_ = null;
    boolean readOnly_ = false;

    public ZTransactStmt(String st) {
        statement_ = new String(st);
    }

    public void setComment(String c) {
        comment_ = new String(c);
    }

    public String getComment() {
        return comment_;
    }

    public boolean isReadOnly() {
        return readOnly_;
    }
};

