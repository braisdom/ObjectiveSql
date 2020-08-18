package com.github.braisdom.jsql;

import com.github.braisdom.jsql.ast.JSqlNode;
import com.github.braisdom.jsql.ast.SymbolRefNode;
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
        JSqlNode JSQLNode =  parser.JSQLNode();

        Assert.assertNotNull(JSQLNode);
        Assert.assertTrue(JSQLNode.getImportNodes().size() == 1);
        Assert.assertTrue(JSQLNode.getImportNodes().get(0).getQualifiedName().equals("com.github.braisdom.dionaea.Func"));
    }

    @Test
    public void testDatasetNode() throws ParseException {
        String importString = "dataset Member(Timestamp purchaseBegin) {\r\n" +
                " projection [:id, :name as user_name],\r\n" +
                "}";
        InputStream is = new ByteArrayInputStream(importString.getBytes(Charset.forName("UTF-8")));
        Parser parser = new Parser(is, Charset.forName("UTF-8").name());
        JSqlNode jsqlNode =  parser.JSQLNode();

        Assert.assertNotNull(jsqlNode);
        Assert.assertTrue(jsqlNode.getDatasetNodes().size() == 1);
        Assert.assertTrue(jsqlNode.getDatasetNodes().get(0).getName().equals("Member"));
        Assert.assertTrue(jsqlNode.getDatasetNodes().get(0).getFormalParameters().size() == 1);
        Assert.assertTrue(jsqlNode.getDatasetNodes().get(0).getFormalParameters().get(0).getName().equals("purchaseBegin"));
        Assert.assertTrue(jsqlNode.getDatasetNodes().get(0).getFormalParameters().get(0).getType().equals("Timestamp"));
        Assert.assertTrue(jsqlNode.getDatasetNodes().get(0).getProjectionals().size() == 2);
        Assert.assertTrue(jsqlNode.getDatasetNodes().get(0).getProjectionals().get(0) instanceof SymbolRefNode);
        Assert.assertTrue(((SymbolRefNode)jsqlNode.getDatasetNodes().get(0).getProjectionals().get(0)).getSymbolName().equals("id"));
        Assert.assertTrue(((SymbolRefNode)jsqlNode.getDatasetNodes().get(0).getProjectionals().get(1)).getSymbolName().equals("name"));
        Assert.assertTrue(((SymbolRefNode)jsqlNode.getDatasetNodes().get(0).getProjectionals().get(1)).getAlias().equals("user_name"));
    }
}
