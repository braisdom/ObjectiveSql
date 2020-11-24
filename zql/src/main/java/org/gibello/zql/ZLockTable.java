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

import java.util.Vector;

/**
 * ZLockTable: an SQL LOCK TABLE statement
 */
public class ZLockTable implements ZStatement {

    boolean nowait_ = false;
    String lockMode_ = null;
    Vector tables_ = null;

    public ZLockTable() {
    }

    public void addTables(Vector v) {
        tables_ = v;
    }

    public Vector getTables() {
        return tables_;
    }

    public void setLockMode(String lc) {
        lockMode_ = new String(lc);
    }

    public String getLockMode() {
        return lockMode_;
    }

    public boolean isNowait() {
        return nowait_;
    }
};

