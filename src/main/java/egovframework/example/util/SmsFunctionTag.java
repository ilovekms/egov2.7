package egovframework.example.util;



public class SmsFunctionTag {
	
    /**
     * 2016.09.20
	 * null인 경우 default 값이 지정한다.
	 * @param String
	 * @return boolean
	 */
    public static String  nvl(String value, String defaultValue)
    {	
    	String val = "";
        if( value == null || value.length() == 0) {
        	val = defaultValue;
        } else {
        	val = value;
        }
        return val;
    }
    
    
    /**
     * 2016.09.20
	 * 글자수에 따른 말줄임
	 * @param String
	 * @param max 글자수
	 * @return boolean
	 */
    public static String  eclipsis(String value, Integer max)
    {	
    	
    	value = nvl(value,"");
 
  	if(value.length() > max) {
    		value = value.substring(0,max)+"...";
    	} 
    	return value;
    	
   }

    
    /**
     * 2016.09.20
	 * yyyyMMdd 을 delimeter 포맷으로 변경 
	 * @param String
	 * @return boolean
	 */
    public static String  changeDateFormat(String value, String delimeter)
    {	
    	if(value == null ) {
    		return "";
    	} else if( value.length() !=8) {
    		return value;
    	} else {
    		//yyyy    MM   dd
    		//0123    45   67
    		return value.substring(0,4)+delimeter +value.substring(4,6)+delimeter +value.substring(6);
    	}
    	
    }
  
}
