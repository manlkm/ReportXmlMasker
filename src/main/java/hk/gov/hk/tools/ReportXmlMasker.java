/**
 * 
 */
package hk.gov.hk.tools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import com.ximpleware.AutoPilot;
import com.ximpleware.ModifyException;
import com.ximpleware.NavException;
import com.ximpleware.TranscodeException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import com.ximpleware.XMLModifier;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;



/**
 * @author manlkm
 *
 */
public class ReportXmlMasker {

	/**
	 * @param args
	 * @throws XPathParseException 
	 * @throws NavException 
	 * @throws XPathEvalException 
	 * @throws ModifyException 
	 * @throws TranscodeException 
	 */
	public static void main(String[] args) throws XPathParseException, XPathEvalException, NavException, ModifyException, TranscodeException {
		System.out.println("ReportXmlMasker starts ...");
		
		if(!(args != null && args.length >= 2)){
			System.out.println("Usage: ReportXmlMasker <input xml path> <output xml path>");
			System.exit(1);
		}
		
		String inXml = args[0];
		String outXml = args[1];
		
		final Properties properties = new Properties();
		try (final InputStream stream = ReportXmlMasker.class.getClassLoader().getResourceAsStream("config.properties")){
			 properties.load(stream);
			 
			 String tagPatterns[] =  ((String) properties.get("tag.pattern")).split(",");
			 String defaultStringReplacer = (String)properties.get("default.replacer");
			 
			 VTDGen vg = new VTDGen();
             if (!vg.parseFile(inXml, false))
                 return;
             VTDNav vn = vg.getNav();
             AutoPilot ap = new AutoPilot(vn);
             
             XMLModifier xm = new XMLModifier(vn);
             
             //testing
             ap.selectXPath("//*/prn/text()");
             int i=ap.evalXPath();
	           while(i!=-1){
	        	   System.out.println(ap.evalXPathToString());
	               xm.updateToken(i, "VVVVV");
	           }
             
//             int index = 0;
//             for(String tagPattern : tagPatterns){
//            	 tagPattern = tagPattern.trim();
//            	 
//            	 String stringReplacer = (String)properties.get("pattern["+index+"].replacer");
//            	 if(stringReplacer == null){
//            		 stringReplacer = defaultStringReplacer;
//            	 }
//            	 
//            	 try{
//            		 Class<?> replacer = Class.forName(stringReplacer);
//            		 Constructor<?> ctor = replacer.getConstructor();
//            		 Object object = ctor.newInstance();
//            		 
//            		 System.out.println("replace " + tagPattern + " using "+ stringReplacer);
//                	 ap.selectXPath(tagPattern);
//                     String newVal = ((ReportXmlStringReplacer)object).replaceString(ap.evalXPathToString());
//                     int i=ap.evalXPath();
//                     if(i!=-1){
//                    	 System.out.println("replaced " + ap.evalXPathToString() + " to : " + newVal);
//                         xm.updateToken(i, newVal);
//                     }
//            	 }catch (Exception e) {
//					e.printStackTrace();
//				}
//                 
//                 index++;
//             }
             xm.output(outXml); 
             
		}catch (IOException e) {
			e.printStackTrace();
		} finally{
			
			System.out.println("ReportXmlMasker completed");
			System.exit(0);
		}

	}

}
