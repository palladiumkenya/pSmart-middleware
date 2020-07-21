package jsonvalidator.main;

import jsonvalidator.mapper.SHR;
import jsonvalidator.utils.Encryption;
import jsonvalidator.utils.SHRUtils;

import java.util.Map;

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
        String test = "A monkey is an animal";
        String encryptedData = Encryption.encrypt(test);
        System.out.println("encryptedData: "+ encryptedData);
        System.out.println("decrypted: "+ Encryption.decrypt(encryptedData));
    }
}
