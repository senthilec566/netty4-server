package com.netty.httpserver;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

/**
 * Compression Utility provide an API's to de-compress or compress 
 * @author senthilec566
 *
 */
public class CompressionUtils {

	public static final String ISO_8859_1 = "ISO-8859-1";
	public static final String UTF_8 = "UTF-8";
	
	public static String decompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        System.out.println("Input String length : " + str.length());
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes(ISO_8859_1)));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, ISO_8859_1));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
          outStr += line;
        }
        System.out.println("Output String lenght : " + outStr.length());
        return outStr;
     }
	
	public static boolean isGZIpCompressed( String data ) throws IOException{
		
		return true;
	}
	
	public static boolean isCompressed(byte[] bytes) throws IOException
	 {
	      if ((bytes == null) || (bytes.length < 2))
	      {
	           return false;
	      }
	      else
	      {
	            return ((bytes[0] == (byte) (GZIPInputStream.GZIP_MAGIC)) && (bytes[1] == (byte) (GZIPInputStream.GZIP_MAGIC >> 8)));
	      }
	 }

	public static String binary2Str(String in) throws UnsupportedEncodingException {
		byte[] encoded = in.getBytes();
		String text = new String(encoded, UTF_8);
		System.out.println("print: " + text);
		return text;
	}
	
}
