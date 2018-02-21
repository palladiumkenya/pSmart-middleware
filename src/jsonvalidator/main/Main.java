package jsonvalidator.main;


import jsonvalidator.apiclient.APIClient;
import jsonvalidator.mapper.SHR;
import jsonvalidator.utils.SHRUtils;

/**
 *
 * @author tedb19
 */
public class Main {
    private static final String SHRURL = "https://my-json-server.typicode.com/tedb19/SHR/SHR";
    
    private Main(){
        throw new UnsupportedOperationException("This operation is forbidden!");
    }
    
    public static void main(String[] args)  {
        //Get an SHR from the EMR
        String SHRStr = APIClient.getSHRStr(SHRURL, "");
        SHR shr = SHRUtils.getSHR(SHRStr);
        System.out.println("CARD STATUS: "+ shr.cARD_DETAILS.sTATUS);
        
        //Post the SHR back to the EMR
        String SHRStrToPost = SHRUtils.getJSON(shr);
        String response = APIClient.postSHR(SHRURL, SHRStrToPost);
        System.out.println(response);        
    }
}
