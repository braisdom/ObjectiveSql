package org.gibello.zql;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import org.gibello.zql.data.ZEval;
import org.gibello.zql.data.ZTuple;
import org.junit.Test;


public class ParserTest {

	@Test
	public void parseTest() {
		try {
			File sql = new File(Thread.currentThread().getContextClassLoader()
				.getResource("test.sql").getFile());
			ZqlParser p = new ZqlParser(new DataInputStream(new FileInputStream(sql)));
		    ZStatement st = null;
		    while((st = p.readStatement()) != null) {
		      System.out.println(st.toString() + ";");
		    }
		} catch(FileNotFoundException e) {	
			fail("File not found: " + e);
		} catch (ParseException e) {
			fail("Parse exception: " + e);
		}
	}
	
	@Test
	public void evalTest() {
		try {
			File dataFile = new File(Thread.currentThread().getContextClassLoader()
					.getResource("eval.data").getFile());
			File expFile = new File(Thread.currentThread().getContextClassLoader()
					.getResource("eval.exp").getFile());
			BufferedReader db = new BufferedReader(
					new FileReader(dataFile));
			String tpl = db.readLine();
			ZTuple t = new ZTuple(tpl);

			ZqlParser parser = new ZqlParser();
			ZEval evaluator = new ZEval();

			while((tpl = db.readLine()) != null) {
				t.setRow(tpl);
				BufferedReader expFileReader = new BufferedReader(new FileReader(expFile));
				String query;
				while((query = expFileReader.readLine()) != null) {
					parser.initParser(new ByteArrayInputStream(query.getBytes()));
					ZExp exp = parser.readExpression();
					System.out.print(tpl + ", " + query + ", ");
					System.out.println(evaluator.eval(t, exp));
				}
				expFileReader.close();
			}
			db.close();
		} catch(FileNotFoundException e) {
			fail("File not found: " + e);
		} catch (IOException e) {
			fail("IO Exception: " + e);
		} catch (ParseException e) {
			fail("Parse exception: " + e);
		} catch (SQLException e) {
			fail("Eval exception: " + e);
		}
	}
}
