package com.hitg.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PhotoService {
	
	public String savePhoto(String imageUrl){
		try{

			URL url;
		    url = new URL(imageUrl);
	        InputStream is = url.openConnection().getInputStream();
	        String imageName = generateImageName(url); 
	        OutputStream outputStream = 
	                    new FileOutputStream(new File(imageName));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = is.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			outputStream.close();
		
			return imageName;			
		} catch(IOException e){
			throw new RuntimeException(e);
			}
		
		}
	
	public String generateImageName(URL imageUrl){
		String host = imageUrl.getHost();
		LocalTime timeNow = LocalTime.now();
		String time = timeNow.format(DateTimeFormatter.ofPattern("HHmmss"));
		String imageName= "../" + host + time+".jpg";
		return imageName;		
	}

}
