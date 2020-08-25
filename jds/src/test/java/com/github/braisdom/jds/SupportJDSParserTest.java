package com.github.braisdom.jds;

import com.github.braisdom.jds.ast.SqlNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class SupportJDSParserTest {

    @Test
    public void testImportNode() throws ParseException {
        String importString = "import com.github.braisdom.dionaea.Func;";
        InputStream is = new ByteArrayInputStream(importString.getBytes(Charset.forName("UTF-8")));
        Parser parser = new Parser(is, Charset.forName("UTF-8").name());
        SqlNode JSQLNode =  parser.SqlNode();

        Assert.assertNotNull(JSQLNode);
        Assert.assertTrue(JSQLNode.getImportNodes().size() == 1);
        Assert.assertTrue(JSQLNode.getImportNodes().get(0).getQualifiedName().equals("com.github.braisdom.dionaea.Func"));
    }

    @Test
    public void testDatasetNode() throws ParseException {
        String importString = "typedef dataset Member(Timestamp purchaseBegin) {\r\n" +
                " projection [ \r\n" +
                "   ((:id + 2) + 3) * :id as id, \r\n" +
                "   #table.name as username, \r\n" +
                "   :name, \r\n" +
                "   Iso.if(:id, max(:id)) as max \r\n" +
                " ],\r\n" +
                " from (Member(:id, 123, 'abc', max()) as demo_table, #table.column), \r\n" +
                " predicate ( (((:id + 10) < 20) == 0) && :name > 0 )" +
                "}";
        InputStream is = new ByteArrayInputStream(importString.getBytes(Charset.forName("UTF-8")));
        Parser parser = new Parser(is, Charset.forName("UTF-8").name());
        SqlNode jsqlNode = parser.SqlNode();

        Assert.assertNotNull(jsqlNode);
        Assert.assertTrue(jsqlNode.getDatasetNodes().size() == 1);
    }
}
