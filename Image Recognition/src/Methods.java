import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Methods {
	
	BufferedImage imageTaught = null;
    float imageArea = 0;
    float imageSides = 0;
    static ArrayList<String> taught = new ArrayList<String>();
    
    ImageCharacteristics IC;
    
    public Methods(ImageCharacteristics IC){
    	this.IC = IC;
    }
	
	public void Review(){
		
		File path = new File("C:\\Users\\aolso\\workspace\\Image Recognition\\Resources\\Taught");
		File [] files = path.listFiles();
		for (int i = 0; i < files.length; i++){
		    if (files[i].isFile()){		    	
		    	
		    	try {
		    		
		    		String imageName = files[i].getName();
		    		String[] splitName = imageName.split("\\s");
		    		String imageTitle = splitName[1];
		    		
		    		File imageFile = new File("C:\\Users\\aolso\\workspace\\Image Recognition\\Resources\\Taught\\" + i + " " + imageTitle);
		    		imageTaught = ImageIO.read(imageFile);//getClass().getResource("/Resources/Images/Square.jpg"));
					
			        
		    		imageArea = IC.imageEvaluateArea(imageTaught);
		    		imageSides = IC.imageEvaluateSides(imageTaught);
			        
			        //System.out.println(imageTitle + " - " + outValue);
			        taught.add(imageTitle + " " + imageArea + " " + imageSides);
			        
			        
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
		    	
		    	
		    }
		}
		
		for (int i = 0; i<taught.size();i++){
			System.out.println(taught.get(i));
		}
		
		
	}
	
	
}
