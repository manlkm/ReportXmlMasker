/**
 * 
 */
package hk.gov.hk.tools;

import java.security.SecureRandom;

/**
 * @author manlkm
 *
 */
public class RandomStringReplacer implements ReportXmlStringReplacer{
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz ";
	static SecureRandom rnd = new SecureRandom();
	
	@Override
	public String replaceString(String originalVal) throws Exception {
		int len = 8;
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   
	   return sb.toString();
	}

}
