package com.github.braisdom.jsql;

import com.github.braisdom.jsql.ast.JSqlNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DionaeaParserTest {

    @Test
    public void testImportNode() throws ParseException {
        String importString = "import com.github.braisdom.dionaea.Func;";
        InputStream is = new ByteArrayInputStream(importString.getBytes(Charset.forName("UTF-8")));
        Parser parser = new Parser(is, Charset.forName("UTF-8").name());
        JSqlNode JSQLNode =  parser.JSqlNode();

        Assert.assertNotNull(JSQLNode);
        Assert.assertTrue(JSQLNode.getImportNodes().size() == 1);
        Assert.assertTrue(JSQLNode.getImportNodes().get(0).getQualifiedName().equals("com.github.braisdom.dionaea.Func"));
    }

    @Test
    public void testDatasetNode() throws ParseException {
        String importString = "typedef dataset Member(Timestamp purchaseBegin) {\r\n" +
                " projection [" +
                "   :id, " +
                "   #tem.name as username, " +
                "   :name, " +
                "   Iso.if(:id is null, '12', fun()) as max" +
                " ]\r\n" +
                "}";
        InputStream is = new ByteArrayInputStream(importString.getBytes(Charset.forName("UTF-8")));
        Parser parser = new Parser(is, Charset.forName("UTF-8").name());
        JSqlNode jsqlNode = parser.JSqlNode();

        Assert.assertNotNull(jsqlNode);
        Assert.assertTrue(jsqlNode.getDatasetNodes().size() == 1);
    }
}
