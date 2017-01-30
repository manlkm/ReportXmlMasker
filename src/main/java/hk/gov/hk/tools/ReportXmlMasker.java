/**
 * 
 */
package hk.gov.hk.tools;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.plist.PropertyListConfiguration;
import org.apache.log4j.Logger;

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
	public static Logger logger = Logger.getLogger(ReportXmlMasker.class);
	
	/**
	 * @param args
	 * @throws XPathParseException 
	 * @throws NavException 
	 * @throws XPathEvalException 
	 * @throws ModifyException 
	 * @throws TranscodeException 
	 * @throws IOException 
	 * @throws ConfigurationException 
	 */
	public static void main(String[] args) throws XPathParseException, XPathEvalException, NavException, ModifyException, TranscodeException, IOException, ConfigurationException {
		logger.info("ReportXmlMasker starts ...");
		
		if(!(args != null && args.length >= 2)){
			System.out.println("Usage: ReportXmlMasker <input xml path> <output xml path>");
			System.exit(1);
		}
		
		String inXml = args[0];
		String outXml = args[1];
		
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
		    new FileBasedConfigurationBuilder<FileBasedConfiguration>
		    (PropertiesConfiguration.class)
		    .configure(params.properties()
		    .setFileName("config.properties"));
		 Configuration config = builder.getConfiguration();
		 
		XMLModifier xm = null;
		try{
			 String tagPatterns[] = config.getStringArray("tag.pattern");
			 String defaultStringReplacer = config.getString("default.replacer");
			 
			 VTDGen vg = new VTDGen();
             if (!vg.parseFile(inXml, false))
                 return;
             VTDNav vn = vg.getNav();
             AutoPilot ap = new AutoPilot(vn);
             xm = new XMLModifier(vn);
             
             int index = 0;
             for(String tagPattern : tagPatterns){
            	 tagPattern = tagPattern.trim();
            	 
            	 String stringReplacer = config.getString("pattern["+index+"].replacer", defaultStringReplacer);
            	 
            	 try{
            		 Class<?> replacer = Class.forName(stringReplacer);
            		 Constructor<?> ctor = replacer.getConstructor();
            		 Object object = ctor.newInstance();
            		 
            		 logger.info("replace " + tagPattern + " using "+ stringReplacer);
                	 ap.selectXPath(tagPattern);
                     
                     int i = ap.evalXPath();
                     while(i != -1){
                    	 String newVal = ((ReportXmlStringReplacer)object).replaceString(vn.toNormalizedString(vn.getText()));
                    	 logger.debug("replaced " + vn.toNormalizedString(vn.getText()) + " to : " + newVal);
                         xm.updateToken(i, newVal);
                         i = ap.evalXPath();
                     }
            	 }catch (Exception e) {
					e.printStackTrace();
				}
                 
                 index++;
             }
             
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}finally{
			xm.output(outXml); 
			logger.info("ReportXmlMasker completed");
			System.exit(0);
		}

	}

}
