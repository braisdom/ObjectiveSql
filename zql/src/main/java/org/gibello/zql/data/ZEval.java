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

import java.sql.SQLException;
import java.util.Vector;

import org.gibello.zql.ZConstant;
import org.gibello.zql.ZExp;
import org.gibello.zql.ZExpression;

/**
 * Evaluate SQL expressions
 */
public class ZEval {

    /**
     * Evaluate a boolean expression to true or false (for example, SQL WHERE
     * clauses are boolean expressions)
     *
     * @param tuple The tuple on which to evaluate the expression
     * @param exp   The expression to evaluate
     * @return true if the expression evaluate to true for this tuple,
     * false if not.
     */
    public boolean eval(ZTuple tuple, ZExp exp) throws SQLException {

        if (tuple == null || exp == null) {
            throw new SQLException("ZEval.eval(): null argument or operator");
        }
        if (!(exp instanceof ZExpression))
            throw new SQLException("ZEval.eval(): only expressions are supported");

        ZExpression pred = (ZExpression) exp;
        String op = pred.getOperator();

        if (op.equals("AND")) {
            boolean and = true;
            for (int i = 0; i < pred.nbOperands(); i++) {
                and &= eval(tuple, pred.getOperand(i));
            }
            return and;
        } else if (op.equals("OR")) {
            boolean or = false;
            for (int i = 0; i < pred.nbOperands(); i++) {
                or |= eval(tuple, pred.getOperand(i));
            }
            return or;
        } else if (op.equals("NOT")) {
            return !eval(tuple, pred.getOperand(0));

        } else if (op.equals("=")) {
            return evalCmp(tuple, pred.getOperands()) == 0;
        } else if (op.equals("!=")) {
            return evalCmp(tuple, pred.getOperands()) != 0;
        } else if (op.equals("<>")) {
            return evalCmp(tuple, pred.getOperands()) != 0;
        } else if (op.equals("#")) {
            throw new SQLException("ZEval.eval(): Operator # not supported");
        } else if (op.equals(">")) {
            return evalCmp(tuple, pred.getOperands()) > 0;
        } else if (op.equals(">=")) {
            return evalCmp(tuple, pred.getOperands()) >= 0;
        } else if (op.equals("<")) {
            return evalCmp(tuple, pred.getOperands()) < 0;
        } else if (op.equals("<=")) {
            return evalCmp(tuple, pred.getOperands()) <= 0;

        } else if (op.equals("BETWEEN") || op.equals("NOT BETWEEN")) {

            // Between: borders included
            ZExpression newexp = new ZExpression("AND",
                    new ZExpression(">=", pred.getOperand(0), pred.getOperand(1)),
                    new ZExpression("<=", pred.getOperand(0), pred.getOperand(2)));

            if (op.equals("NOT BETWEEN"))
                return !eval(tuple, newexp);
            else
                return eval(tuple, newexp);

        } else if (op.equals("LIKE") || op.equals("NOT LIKE")) {
            boolean like = evalLike(tuple, pred.getOperands());
            return op.equals("LIKE") ? like : !like;

        } else if (op.equals("IN") || op.equals("NOT IN")) {

            ZExpression newexp = new ZExpression("OR");

            for (int i = 1; i < pred.nbOperands(); i++) {
                newexp.addOperand(new ZExpression("=",
                        pred.getOperand(0), pred.getOperand(i)));
            }

            if (op.equals("NOT IN"))
                return !eval(tuple, newexp);
            else
                return eval(tuple, newexp);

        } else if (op.equals("IS NULL")) {

            if (pred.nbOperands() <= 0 || pred.getOperand(0) == null) return true;
            ZExp x = pred.getOperand(0);
            if (x instanceof ZConstant) {
                return (((ZConstant) x).getType() == ZConstant.NULL);
            } else {
                throw new SQLException("ZEval.eval(): can't eval IS (NOT) NULL");
            }

        } else if (op.equals("IS NOT NULL")) {

            ZExpression x = new ZExpression("IS NULL");
            x.setOperands(pred.getOperands());
            return !eval(tuple, x);

        } else {
            throw new SQLException("ZEval.eval(): Unknown operator " + op);
        }

    }

    double evalCmp(ZTuple tuple, Vector operands) throws SQLException {

        if (operands.size() < 2) {
            throw new SQLException(
                    "ZEval.evalCmp(): Trying to compare less than two values");
        }
        if (operands.size() > 2) {
            throw new SQLException(
                    "ZEval.evalCmp(): Trying to compare more than two values");
        }

        Object o1 = null, o2 = null, obj;

        o1 = evalExpValue(tuple, (ZExp) operands.elementAt(0));
        o2 = evalExpValue(tuple, (ZExp) operands.elementAt(1));

        if (o1 instanceof String || o2 instanceof String) {
            return (o1.equals(o2) ? 0 : -1);
        }

        if (o1 instanceof Number && o2 instanceof Number) {
            return ((Number) o1).doubleValue() - ((Number) o2).doubleValue();
        } else {
            throw new SQLException("ZEval.evalCmp(): can't compare (" + o1.toString()
                    + ") with (" + o2.toString() + ")");
        }
    }


    // -------------------------------------------------------------------------

    /**
     * evalLike
     * evaluates the LIKE operand
     *
     * @param tuple    the tuple to evaluate
     * @param operands the operands
     * @return true-> the expression matches
     * @throws SQLException
     */
    private boolean evalLike(ZTuple tuple, Vector operands) throws SQLException {
        if (operands.size() < 2) {
            throw new SQLException(
                    "ZEval.evalCmp(): Trying to compare less than two values");
        }
        if (operands.size() > 2) {
            throw new SQLException(
                    "ZEval.evalCmp(): Trying to compare more than two values");
        }

        Object o1 = evalExpValue(tuple, (ZExp) operands.elementAt(0));
        Object o2 = evalExpValue(tuple, (ZExp) operands.elementAt(1));

        if ((o1 instanceof String) && (o2 instanceof String)) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            if (s2.startsWith("%")) {
                return s1.endsWith(s2.substring(1));
            } else if (s2.endsWith("%")) {
                return s1.startsWith(s2.substring(0, s2.length() - 1));
            } else {
                return s1.equalsIgnoreCase(s2);
            }
        } else {
            throw new SQLException("ZEval.evalLike(): LIKE can only compare strings");
        }

    }

    double evalNumericExp(ZTuple tuple, ZExpression exp)
            throws SQLException {

        if (tuple == null || exp == null || exp.getOperator() == null) {
            throw new SQLException("ZEval.eval(): null argument or operator");
        }

        String op = exp.getOperator();

        Object o1 = evalExpValue(tuple, (ZExp) exp.getOperand(0));
        if (!(o1 instanceof Double))
            throw new SQLException("ZEval.evalNumericExp(): expression not numeric");
        Double dobj = (Double) o1;

        if (op.equals("+")) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                Object obj = evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val += ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals("-")) {

            double val = dobj.doubleValue();
            if (exp.nbOperands() == 1) return -val;
            for (int i = 1; i < exp.nbOperands(); i++) {
                Object obj = evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val -= ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals("*")) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                Object obj = evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val *= ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals("/")) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                Object obj = evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val /= ((Number) obj).doubleValue();
            }
            return val;

        } else if (op.equals("**")) {

            double val = dobj.doubleValue();
            for (int i = 1; i < exp.nbOperands(); i++) {
                Object obj = evalExpValue(tuple, (ZExp) exp.getOperand(i));
                val = Math.pow(val, ((Number) obj).doubleValue());
            }
            return val;

        } else {
            throw new SQLException("ZEval.evalNumericExp(): Unknown operator " + op);
        }
    }


    /**
     * Evaluate a numeric or string expression (example: a+1)
     *
     * @param tuple The tuple on which to evaluate the expression
     * @param exp   The expression to evaluate
     * @return The expression's value
     */
    public Object evalExpValue(ZTuple tuple, ZExp exp) throws SQLException {

        Object o2 = null;

        if (exp instanceof ZConstant) {

            ZConstant c = (ZConstant) exp;

            switch (c.getType()) {

                case ZConstant.COLUMNNAME:

                    Object o1 = tuple.getAttValue(c.getValue());
                    if (o1 == null)
                        throw new SQLException("ZEval.evalExpValue(): unknown column "
                                + c.getValue());
                    try {
                        o2 = new Double(o1.toString());
                    } catch (NumberFormatException e) {
                        o2 = o1;
                    }
                    break;

                case ZConstant.NUMBER:
                    o2 = new Double(c.getValue());
                    break;

                case ZConstant.STRING:
                default:
                    o2 = c.getValue();
                    break;
            }
        } else if (exp instanceof ZExpression) {
            o2 = new Double(evalNumericExp(tuple, (ZExpression) exp));
        }
        return o2;
    }

}

