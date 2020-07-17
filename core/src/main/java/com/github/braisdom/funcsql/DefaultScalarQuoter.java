package com.github.braisdom.funcsql;

public class DefaultScalarQuoter implements ScalarQuoter {

    @Override
    public String quote(Object... scalars) {
        StringBuilder sb = new StringBuilder();

        for (Object value : scalars) {
            if (value instanceof Integer || value instanceof Long ||
                    value instanceof Float || value instanceof Double)
                sb.append(String.valueOf(value));
            else
                sb.append(String.format("'%s'", String.valueOf(value)));
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }

}
