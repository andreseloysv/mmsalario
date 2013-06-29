package domain.mmcontrolclass;

import java.security.NoSuchAlgorithmException;
//import java.util.HashMap;
//import javax.servlet.http.HttpServletRequest;
import dinamica.*;

public class ClaveEncryptor {
	
	public String encryptor(String userid, String password){
		
		//create MD5 hash using the string: userlogin:passwd
		java.security.MessageDigest md;
		String pwd = "";
		
		try {
			md = java.security.MessageDigest.getInstance("MD5");
			byte[] b = (userid + ":" + password).getBytes();
			byte[] hash = md.digest(b);
			pwd = Base64.encodeToString( hash, true );
			
			return pwd;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pwd;
	}
}
