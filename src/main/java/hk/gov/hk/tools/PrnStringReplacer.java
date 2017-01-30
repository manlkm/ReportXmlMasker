/**
 * 
 */
package hk.gov.hk.tools;

/**
 * @author manlkm
 *
 */
public class PrnStringReplacer implements ReportXmlStringReplacer{
	private static String[] prnInit = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	private static int nextPoolIdx = 0;
	private static int nextInt = 1;
	private static final int maxPrn = 9999999;
	
	@Override
	public String replaceString(String originalVal) throws Exception {
		String rtnVal = null;
		if(nextInt >=maxPrn){
			nextPoolIdx++;
			nextInt = 0;
		}
		
		rtnVal = " " + prnInit[nextPoolIdx]+leftPadValue(String.valueOf(nextInt++), "0", 7);
		
		if(rtnVal.equals(" " + prnInit[prnInit.length-1] + maxPrn)){
			nextPoolIdx = 0;
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
