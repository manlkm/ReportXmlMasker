/**
 * 
 */
package hk.gov.hk.tools;

/**
 * @author manlkm
 *
 */
public class BrnStringReplacer implements ReportXmlStringReplacer{
	private static int nextInt = 1;
	private static final int maxBrn = 99999999;
	
	@Override
	public String replaceString(String originalVal) throws Exception {
		String rtnVal = null;
		if(nextInt >=maxBrn){
			nextInt = 0;
		}
		
		rtnVal = leftPadValue(String.valueOf(nextInt++), "0", 8);
		
		if(rtnVal.equals(maxBrn)){
			nextInt = 0;
		}
		
		return rtnVal;
	}

	private String leftPadValue(String oriVal, String valToPad, int maxLength){
		int lenToPad = maxLength - String.valueOf(oriVal).length();
		if(lenToPad > 0){
			for(int i=1; i<=lenToPad; i++){
				oriVal=valToPad+oriVal;
			}
		}
		
		return oriVal;
	}

}
