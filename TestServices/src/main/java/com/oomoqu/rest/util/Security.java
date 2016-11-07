package com.oomoqu.rest.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Security {
	public static String getEncryptedPasswordWithHMAC(String value)   {  
		 try {
	            // Get an hmac_sha1 key from the raw key bytes
			    String key = "213213dsasdfsdab324324dsfa";
			    Mac mac = Mac.getInstance("HmacSHA256");
	            byte[] keyBytes = key.getBytes();           
	            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

	            // Get an hmac_sha1 Mac instance and initialize with the signing key
	            
	            mac.init(signingKey);

	            // Compute the hmac on input data bytes
	            byte[] rawHmac = mac.doFinal(Base64.decodeBase64(value));
	            return Base64.encodeBase64String(rawHmac);
	            
	            
	            //return rawHmac;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
		  }
	
		public static void main(String[] args){
			System.out.println(Base64.encodeBase64String("fenilabc".getBytes()));
			System.out.println(getEncryptedPasswordWithHMAC(Base64.encodeBase64String("fenilabc".getBytes())));
			System.out.println(new String(Base64.decodeBase64("YWJjNTQzMjE=".getBytes())));
		}
}
