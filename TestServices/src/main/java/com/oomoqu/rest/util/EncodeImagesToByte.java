package com.oomoqu.rest.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@Component
public class EncodeImagesToByte {
	private static Logger logger = Logger.getLogger(EncodeImagesToByte.class);
	/**
	 * @param couponId
	 * @return
	 */
	public String encodeImageToString(String couponId){
		
		File file = new File(couponId);
		if(!file.exists()){
			return null;
		}
			
		String imageDataString = null;

		try {           
            FileInputStream imageInFile = new FileInputStream(file);
            byte imageData[] = new byte[(int) file.length()];

            imageInFile.read(imageData);
            imageDataString = EncodeImage(imageData);
            imageInFile.close();
            
            file.delete();
            
            /**
            if(file.delete()){
    			logger.debug(	file.getName() + " is deleted!");
    		} else {
    			logger.debug("Delete operation is failed.");
    		}
			*/
        } catch (FileNotFoundException e) {
        	logger.error("Barcode Image not found" + e);
        } catch (IOException ioe) {
        	logger.error("Exception while reading the Barcode Image " + ioe);
        }
		
        return imageDataString;
	}

	private String EncodeImage(byte[] imageData) {
		return Base64.encodeBase64String(imageData);/*Base64.encodeBase64URLSafeString(imageData);*/
	}
}
