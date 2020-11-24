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
 * ZExpression: an SQL Expression
 * An SQL expression is an operator and one or more operands
 * Example: a AND b AND c -> operator = AND, operands = (a, b, c)
 */
public class ZExpression implements ZExp {

    String op_ = null;
    Vector operands_ = null;

    /**
     * Create an SQL Expression given the operator
     *
     * @param op The operator
     */
    public ZExpression(String op) {
        op_ = new String(op);
    }

    /**
     * Create an SQL Expression given the operator and 1st operand
     *
     * @param op The operator
     * @param o1 The 1st operand
     */
    public ZExpression(String op, ZExp o1) {
        op_ = new String(op);
        addOperand(o1);
    }

    /**
     * Create an SQL Expression given the operator, 1st and 2nd operands
     *
     * @param op The operator
     * @param o1 The 1st operand
     * @param o2 The 2nd operand
     */
    public ZExpression(String op, ZExp o1, ZExp o2) {
        op_ = new String(op);
        addOperand(o1);
        addOperand(o2);
    }

    /**
     * Get this expression's operator.
     *
     * @return the operator.
     */
    public String getOperator() {
        return op_;
    }

    /**
     * Set the operands list
     *
     * @param v A vector that contains all operands (ZExp objects).
     */
    public void setOperands(Vector v) {
        operands_ = v;
    }

    /**
     * Get this expression's operands.
     *
     * @return the operands (as a Vector of ZExp objects).
     */
    public Vector getOperands() {
        return operands_;
    }

    /**
     * Add an operand to the current expression.
     *
     * @param o The operand to add.
     */
    public void addOperand(ZExp o) {
        if (operands_ == null) operands_ = new Vector();
        operands_.addElement(o);
    }

    /**
     * Get an operand according to its index (position).
     *
     * @param pos The operand index, starting at 0.
     * @return The operand at the specified index, null if out of bounds.
     */
    public ZExp getOperand(int pos) {
        if (operands_ == null || pos >= operands_.size()) return null;
        return (ZExp) operands_.elementAt(pos);
    }

    /**
     * Get the number of operands
     *
     * @return The number of operands
     */
    public int nbOperands() {
        if (operands_ == null) return 0;
        return operands_.size();
    }

    /**
     * String form of the current expression (reverse polish notation).
     * Example: a > 1 AND b = 2 -> (AND (> a 1) (= b 2))
     *
     * @return The current expression in reverse polish notation (a String)
     */
    public String toReversePolish() {
        StringBuffer buf = new StringBuffer("(");
        buf.append(op_);
        for (int i = 0; i < nbOperands(); i++) {
            ZExp opr = getOperand(i);
            if (opr instanceof ZExpression)
                buf.append(" " + ((ZExpression) opr).toReversePolish()); // Warning recursive call
            else if (opr instanceof ZQuery)
                buf.append(" (" + opr.toString() + ")");
            else
                buf.append(" " + opr.toString());
        }
        buf.append(")");
        return buf.toString();
    }

    public String toString() {

        if (op_.equals("?")) return op_; // For prepared columns ("?")

        if (ZUtils.isCustomFunction(op_) >= 0)
            return formatFunction();

        StringBuffer buf = new StringBuffer();
        if (needPar(op_)) buf.append("(");

        ZExp operand;
        switch (nbOperands()) {

            case 1:
                operand = getOperand(0);
                if (operand instanceof ZConstant) {
                    // Operator may be an aggregate function (MAX, SUM...)
                    if (ZUtils.isAggregate(op_))
                        buf.append(op_ + "(" + operand.toString() + ")");
                    else if (op_.equals("IS NULL") || op_.equals("IS NOT NULL"))
                        buf.append(operand.toString() + " " + op_);
                        // "," = list of values, here just one single value
                    else if (op_.equals(",")) buf.append(operand.toString());
                    else buf.append(op_ + " " + operand.toString());
                } else if (operand instanceof ZQuery) {
                    buf.append(op_ + " (" + operand.toString() + ")");
                } else {
                    if (op_.equals("IS NULL") || op_.equals("IS NOT NULL"))
                        buf.append(operand.toString() + " " + op_);
                        // "," = list of values, here just one single value
                    else if (op_.equals(",")) buf.append(operand.toString());
                    else buf.append(op_ + " " + operand.toString());
                }
                break;

            case 3:
                if (op_.toUpperCase().endsWith("BETWEEN")) {
                    buf.append(getOperand(0).toString() + " " + op_ + " "
                            + getOperand(1).toString()
                            + " AND " + getOperand(2).toString());
                    break;
                }

            default:

                boolean in_op = op_.equals("IN") || op_.equals("NOT IN");

                int nb = nbOperands();
                for (int i = 0; i < nb; i++) {

                    if (in_op && i == 1) buf.append(" " + op_ + " (");

                    operand = getOperand(i);
                    if (operand instanceof ZQuery && !in_op) {
                        buf.append("(" + operand.toString() + ")");
                    } else {
                        buf.append(operand.toString());
                    }
                    if (i < nb - 1) {
                        if (op_.equals(",") || (in_op && i > 0)) buf.append(", ");
                        else if (!in_op) buf.append(" " + op_ + " ");
                    }
                }
                if (in_op) buf.append(")");
                break;
        }

        if (needPar(op_)) buf.append(")");
        return buf.toString();
    }

    private boolean needPar(String op) {
        String tmp = op.toUpperCase();
        return !(tmp.equals("ANY") || tmp.equals("ALL")
                || tmp.equals("UNION") || ZUtils.isAggregate(tmp));
    }

    private String formatFunction() {
        StringBuffer b = new StringBuffer(op_ + "(");
        int nb = nbOperands();
        for (int i = 0; i < nb; i++) {
            b.append(getOperand(i).toString() + (i < nb - 1 ? "," : ""));
        }
        b.append(")");
        return b.toString();
    }
};

