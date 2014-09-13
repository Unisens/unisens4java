package org.unisens.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TestUtils implements TestProperties {
	
	   public static boolean deleteRecursive(File path) throws FileNotFoundException{
	        if (path.exists())
	        {
	        	boolean ret = true;
	        
		        if (path.isDirectory()){
		            for (File f : path.listFiles()){
		                ret = ret && deleteRecursive(f);
		            }
		        }
		        return ret && path.delete();
	        }
	        else 
	        {
	        	return false;
	        }
	    }
	    
	    public static String copyTestData(String source)
	    {
	    	String dest = TEST_DEST+System.getProperty("file.separator");
	    	new File(TEST_DEST).mkdir();
	    	try {
	    		copyDirectory(new File(TEST_SRC_BASE+System.getProperty("file.separator")+source), new File(TEST_DEST+System.getProperty("file.separator")+source));
	    		return TEST_DEST+System.getProperty("file.separator")+source;
	    	} catch (IOException e) {
	    		return null;
	    	}
	    }
	    
	    public static void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
	        if (sourceLocation.isDirectory()) {
	            if (!targetLocation.exists()) {
	                targetLocation.mkdir();
	            }

	            String[] children = sourceLocation.list();
	            for (int i=0; i<children.length; i++) {
	                copyDirectory(new File(sourceLocation, children[i]),
	                        new File(targetLocation, children[i]));
	            }
	        } else {

	            InputStream in = new FileInputStream(sourceLocation);
	            OutputStream out = new FileOutputStream(targetLocation);

	            // Copy the bits from instream to outstream
	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }
	            in.close();
	            out.close();
	        }
	    }

}
