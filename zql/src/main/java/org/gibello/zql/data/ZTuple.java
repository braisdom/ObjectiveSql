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

package org.gibello.zql.data;

import java.util.Vector;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ZTuple {

    /**
     * the names of the attributes
     */
    private Vector attributes_;
    /**
     * the values of the attributes
     */
    private Vector values_;
    /**
     * hashtable to locate attribute names more easily
     */
    private Hashtable searchTable_;
    /**
     * Separator char for column names and values
     */
    private String separator_ = ",";


    /**
     * The simplest constructor
     */
    public ZTuple() {
        attributes_ = new Vector();
        values_ = new Vector();
        searchTable_ = new Hashtable();
    }

    /**
     * Constructor with custom separator char
     */
    public ZTuple(char separator) {
        this();
        separator_ = Character.toString(separator);
    }

    /**
     * Create a new tuple, given it's column names
     *
     * @param colnames Column names separated by separator char (default ,).
     */
    public ZTuple(String colnames) {
        this();
        setColnames(colnames);
    }

    /**
     * Set the current tuple's column values.
     *
     * @param row Column values separated by separator char (default ,).
     */
    public void setColnames(String colnames) {
        StringTokenizer st = new StringTokenizer(colnames, separator_);
        while (st.hasMoreTokens()) {
            setAtt(st.nextToken().trim(), null);
        }
    }

    /**
     * Set the current tuple's column values.
     *
     * @param row Column values separated by separator char (default ,).
     */
    public void setRow(String row) {
        String[] tokens = row.split(separator_);
        for (int i = 0; i < tokens.length; i++) {
            try {
                Double d = new Double(tokens[i].trim());
                setAtt(getAttName(i), d);
            } catch (Exception e) {
                setAtt(getAttName(i), tokens[i].trim());
            }
        }
    }

    /**
     * Set the current tuple's column values.
     *
     * @param row A vector of column values.
     */
    public void setRow(Vector row) {
        for (int i = 0; i < row.size(); i++) {
            setAtt(getAttName(i), row.elementAt(i));
        }
    }


    /**
     * Set the value of the given attribute name
     *
     * @param name  the string representing the attribute name
     * @param value the Object representing the attribute value
     */
    public void setAtt(String name, Object value) {
        if (name != null) {
            boolean exist = searchTable_.containsKey(name);

            if (exist) {
                int i = ((Integer) searchTable_.get(name)).intValue();
                values_.setElementAt(value, i);
            } else {
                int i = attributes_.size();
                attributes_.addElement(name);
                values_.addElement(value);
                searchTable_.put(name, new Integer(i));
            }
        }
    }

    /**
     * Return the name of the attribute corresponding to the index
     *
     * @param index integer giving the index of the attribute
     * @return a String
     */
    public String getAttName(int index) {
        try {
            return (String) attributes_.elementAt(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Return the index  of the attribute corresponding to the name
     *
     * @param index integer giving the index of the attribute
     * @return the index as an int, -1 if name is not an attribute
     */
    public int getAttIndex(String name) {
        if (name == null)
            return -1;
        Integer index = (Integer) searchTable_.get(name);
        if (index != null)
            return index.intValue();
        else
            return -1;
    }

    /**
     * Return the value of the attribute corresponding to the index
     *
     * @param index integer giving the index of the attribute
     * @return an Object (null if index is out of bound)
     */
    public Object getAttValue(int index) {
        try {
            return values_.elementAt(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Return the value of the attribute whith the given name
     *
     * @return an Object (null if name is not an existing attribute)
     */
    public Object getAttValue(String name) {
        boolean exist = false;

        if (name != null)
            exist = searchTable_.containsKey(name);

        if (exist) {
            int index = ((Integer) searchTable_.get(name)).intValue();
            return values_.elementAt(index);
        } else
            return null;
    }

    /**
     * To know if an attributes is already defined
     *
     * @param attrName the name of the attribute
     * @return true if there, else false
     */
    public boolean isAttribute(String attrName) {
        if (attrName != null)
            return searchTable_.containsKey(attrName);
        else
            return false;
    }

    /**
     * Return the number of attributes in the tupple
     *
     * @return int the number of attributes
     */
    public int getNumAtt() {
        return values_.size();
    }

    /**
     * Returns a string representation of the object
     *
     * @return a string representation of the object
     */
    public String toString() {
        Object att;
        Object value;
        String attS;
        String valueS;

        StringBuffer resp = new StringBuffer();
        resp.append("[");
        if (attributes_.size() > 0) {
            att = attributes_.elementAt(0);
            if (att == null)
                attS = "(null)";
            else
                attS = att.toString();

            value = values_.elementAt(0);
            if (value == null)
                valueS = "(null)";
            else
                valueS = value.toString();
            resp.append(attS + " = " + valueS);
        }

        for (int i = 1; i < attributes_.size(); i++) {
            att = attributes_.elementAt(i);
            if (att == null)
                attS = "(null)";
            else
                attS = att.toString();

            value = values_.elementAt(i);
            if (value == null)
                valueS = "(null)";
            else
                valueS = value.toString();
            resp.append(", " + attS + " = " + valueS);
        }
        resp.append("]");
        return resp.toString();
    }

};

