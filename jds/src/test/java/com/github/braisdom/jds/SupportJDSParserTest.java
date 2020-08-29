package com.github.braisdom.jds;

import org.junit.Test;

public class SupportJDSParserTest {

    @Test
    public void testImportNode() {

    }
//
//    @Test
//    public void testDatasetNode() throws ParseException {
//        String importString = "typedef dataset Member(Timestamp purchaseBegin) {\r\n" +
//                " projection [ \r\n" +
//                "   1 -> 'integer_column'," +
//                "   -1 -> 'negative_column'," +
//                "   0.1 -> 'decimals_column'," +
//                "   null -> 'null_column'," +
//                "   :column -> 'symbol_column'," +
//                "   #table.column -> 'complex_symbol_column'," +
//                "   func() -> 'func_call_column'," +
//                "   func(:column) -> 'func_call_column2'," +
//                "   'string' -> 'string_column'" +
//                " ],\r\n" +
//                " from #database.sample_table, \r\n" +
//                " predicate ( :member_id < 10 && :member_id == 10 )" +
//                "}";
//        InputStream is = new ByteArrayInputStream(importString.getBytes(Charset.forName("UTF-8")));
//        Parser parser = new Parser(is, Charset.forName("UTF-8").name());
//        SqlNode jsqlNode = parser.SqlNode();
//
//        Assert.assertNotNull(jsqlNode);
//        Assert.assertTrue(jsqlNode.getDatasetNodes().size() == 1);
//    }
}
