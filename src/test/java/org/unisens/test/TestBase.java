package org.unisens.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

public class TestBase implements TestProperties {
	
	@Before
	public void initialize() throws FileNotFoundException
	{
		if (new File(TEST_DEST).exists()) {
			assertTrue(TestUtils.deleteRecursive(new File(TEST_DEST)));
		}
	}
	
	@After
	public void cleanup() throws FileNotFoundException
	{
		if (new File(TEST_DEST).exists()) {
			assertTrue(TestUtils.deleteRecursive(new File(TEST_DEST)));
		}
	}
	
 
    
}
