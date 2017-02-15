import java.awt.Color;
import java.awt.image.BufferedImage;
import java.sql.Array;

public class ImageCharacteristics {
	
	private DrawArea DA;
	
	private int width;
	private int height;
	
	float prevSlope;
	
	public ImageCharacteristics(DrawArea DA) {
		this.DA = DA;
		
	}

	
	public float imageEvaluateArea(BufferedImage image){
		width = image.getWidth();
        height = image.getHeight();
        float outAmount = 0;        
        for(int y=0; y<height; y++){
           for(int x=0; x<width; x++){        	  
        	  //System.out.println("X: " + x + "  Y: " + y);
              Color c = new Color(image.getRGB(x, y), false);
              //System.out.println("c.getBlue(): "+c.getBlue());
              double colorLuminance = 0.2126*c.getRed() + 0.7152*c.getGreen() + 0.0722*c.getBlue();
              boolean black = colorLuminance < 128 ? true : false;
              if (black){
            	  outAmount += 1;
              }              
           }
        }
        return outAmount;
	}
	
	public float imageEvaluateSides(BufferedImage image){
		width = image.getWidth();
        height = image.getHeight();
        float outAmount = 1;// must have at least 1 side      
        
        
        
        
        int[][] sidePoints = new int[width*height][2];
        
        
        
        
        for(int y=0; y<height; y++){
        	boolean prevBlack = false;
            for(int x=0; x<width; x++){
              Color c = new Color(image.getRGB(x, y), false);
              double colorLuminance = 0.2126*c.getRed() + 0.7152*c.getGreen() + 0.0722*c.getBlue();
              boolean black = colorLuminance < 128 ? true : false;
              
              if (black&&!prevBlack){
            	  int i = 0;
            	  while(sidePoints[i][1]!=0){
            		  i++;
            	  }
            	  sidePoints[i][0]=x+1;//+1 to ensure that it will only be 0 if not set
            	  sidePoints[i][1]=y+1;
              }
              
              prevBlack = black;
           }
        }
        
        float[][] slopes = new float[sidePoints.length][3];// [0] = slope, [1] = x, [2] = y
        for(int i=0;i<sidePoints.length;i++){
        	if (sidePoints[i][0]!=0){
        		
        		int numberInRange = 0;
        		float totalSlope = 0;
        		float totalX = 0;
        		float totalY = 0;
        		for (int j=0;j<sidePoints.length;j++){
        			
        			int maxRange = 15;
        			int minRange = 5;
        			
        			if (Math.abs(sidePoints[j][0]-sidePoints[i][0])<maxRange && Math.abs(sidePoints[j][1]-sidePoints[i][1])<maxRange && (Math.abs(sidePoints[j][0]-sidePoints[i][0])>minRange || Math.abs(sidePoints[j][1]-sidePoints[i][1])>minRange)){// Check if in range
        				
        				if ((float) (sidePoints[j][0]-sidePoints[i][0]) != 0){
        					
        					totalSlope += (float) (sidePoints[i][1]-sidePoints[j][1])/(sidePoints[j][0]-sidePoints[i][0]);
            				totalX += sidePoints[i][0];
            				totalY += sidePoints[i][1];
        					
            				//System.out.println("totalSlope += " + (float) (sidePoints[i][1]-sidePoints[j][1])/(sidePoints[j][0]-sidePoints[i][0]));
            				numberInRange++;
        					
        					
        				}else{
        					//System.out.println("Vertical");
        				}
        				
        				
        			}
        		}
        		
        		int s = 0;
        		while (slopes[s][0] != 0){// find lowest empty index in slopes
        			s++;
        		}
        		
        		if (numberInRange!=0){
        			//System.out.println("================================Average Slope= " + (totalSlope/numberInRange));
        			slopes[s][0] = (float) (totalSlope/numberInRange);
        			slopes[s][1] = (float) (totalX/numberInRange);
        			slopes[s][2] = (float) (totalY/numberInRange);
        		}else{
        			//System.out.println("Average Slope= Vertical");
        			//slopes[s] = (float) (10);
        		}
        		
        	}else{
        		break;
        	}
        }
        
        int s = 0;
		while (slopes[s][0] != 0){// find lowest empty index in slopes
			s++;
		}
        
        float tolerance = (float) 1;
        float checkSlope = 0;// Slope to check for
        float[][][] similarSlopes = new float[s][s][3];// [index of different slopes][index of points with similar slope][0=slope,1=x,2=y]
        int slopeIndex = 0;// current slope to attribute points to
		
        int emptySlopeIndex = 0;
        int emptyPointIndex = 0;
        int emptyIndex = 0;

		checkSlope = slopes[0][0];
		similarSlopes[slopeIndex][0][0] = slopes[0][0];// add starting point's slope to similarSlopes 
		similarSlopes[slopeIndex][0][1] = slopes[0][1];// add starting point's x to similarSlopes 
		similarSlopes[slopeIndex][0][2] = slopes[0][2];// add starting point's y to similarSlopes 
		int epoch = 1;// count # of times through while loop
        while (true){
			
        	System.out.println("----------------------------------------------------Epoch #" + epoch + "--------------------------------------------------");

			emptyIndex = 0;
			while (slopes[emptyIndex][0] != 0){// find lowest empty index in slopes
				emptyIndex++;
			}
			
        	for (int c=0;c<emptyIndex && c<s;c++){
        		
        		if ((Math.abs(slopes[c][0]-checkSlope) < tolerance)){// if point is parallel to previously checked point
    				
    				checkSlope = (float) (checkSlope + slopes[c][0])/2;
    				System.out.println("In line");
    				
    				emptyPointIndex = 0;
    				while (similarSlopes[slopeIndex][emptyPointIndex][0] != 0){// find lowest empty point index in similarSlopes in slope index 0
						if (emptyPointIndex < similarSlopes[slopeIndex].length){
							emptyPointIndex++;
						}else{
							break;
						}
    				}
    				similarSlopes[slopeIndex][emptyPointIndex][0] = slopes[c][0];// move point to similarSlopes 
    				similarSlopes[slopeIndex][emptyPointIndex][1] = slopes[c][1]; 
    				similarSlopes[slopeIndex][emptyPointIndex][2] = slopes[c][2]; 
    				
    				emptyIndex = 0;
    				while (slopes[emptyIndex][0] != 0){// find lowest empty index in slopes
    					emptyIndex++;
    				}
    				//System.out.println("before slopes length of " + emptyIndex);
    				for (int i3=c;i3<emptyIndex;i3++){// remove point from slopes
    					slopes[i3][0] = slopes[i3+1][0];
    					slopes[i3][1] = slopes[i3+1][1];
    					slopes[i3][2] = slopes[i3+1][2];
    				}
    				emptyIndex = 0;
    				while (slopes[emptyIndex][0] != 0){// find lowest empty index in slopes
    					emptyIndex++;
    				}
    				//System.out.println("after slopes length of " + emptyIndex);
    				
    				
    				
    			}else{// if point is not parallel to previously checked point
    				emptySlopeIndex = 0;
    				while (similarSlopes[emptySlopeIndex][0][0] != 0){// find lowest empty slope index in similarSlopes
    					emptySlopeIndex++;
    				}
    				if (emptySlopeIndex != 0){// if emptySlopeIndex = 0 then the first new slope index must be 1
    					int checkFit = 0;
    					for (int c2 = 0;c2 < emptySlopeIndex; c2++){
    						
    						
    						emptyPointIndex = 0;
    						if (c2 < similarSlopes.length){
	    						while (similarSlopes[c2][emptyPointIndex][0] != 0){// find lowest empty point index in similarSlopes in slope index c2
	    							if (emptyPointIndex < similarSlopes[c2].length){
	    								emptyPointIndex++;
	    							}
	    						}
    						}else{
    							System.out.println(c2 + " and " + emptySlopeIndex + " was greater than " + similarSlopes.length);
    						}
    						float slopeTotal = 0;//find average slope in each existing slope index
    						for (int i2=0;i2 < emptyPointIndex;i2++){
    							slopeTotal += similarSlopes[c2][i2][0];
    						}
    						checkSlope = slopeTotal/(emptyPointIndex-1);
    						slopeIndex = c2;
    						System.out.println("checkSlope "+checkSlope);
	    					if ((Math.abs(slopes[c][0]-checkSlope) < tolerance)){// if it already fits in a slope index
	    						System.out.println("Already fits");
	    						emptyPointIndex = 0;
	    	    				while (similarSlopes[slopeIndex][emptyPointIndex][0] != 0){// find lowest empty point index in similarSlopes in slope index 0
	    	    					emptyPointIndex++;
	    	    				}
	    						similarSlopes[slopeIndex][emptyPointIndex][0] = slopes[c][0];// move point to similarSlopes 
	    	    				similarSlopes[slopeIndex][emptyPointIndex][1] = slopes[c][1]; 
	    	    				similarSlopes[slopeIndex][emptyPointIndex][2] = slopes[c][2];

	    	    				emptyIndex = 0;
	            				while (slopes[emptyIndex][0] != 0){// find lowest empty index in slopes
	            					emptyIndex++;
	            				}
	            				
	            				for (int i3=c;i3<emptyIndex;i3++){// remove point from slopes
	            					slopes[i3][0] = slopes[i3+1][0];
	            					slopes[i3][1] = slopes[i3+1][1];
	            					slopes[i3][2] = slopes[i3+1][2];
	            				}
	    						
	    						checkFit++;
	    					}
	    				}
    					
    					if (checkFit ==0){// if it does not fit in an existing slope index
    						checkSlope = (float) (slopes[c][0]);
    						slopeIndex = emptySlopeIndex;
    						System.out.println("New slope index for slope "+ slopes[c][0]);
    						emptyPointIndex = 0;
    	    				while (similarSlopes[slopeIndex][emptyPointIndex][0] != 0){// find lowest empty point index in similarSlopes in slope index 0
    	    					if (emptyPointIndex < similarSlopes[slopeIndex].length){
    								emptyPointIndex++;
    							}else{
    								break;
    							}
    	    				}
    						similarSlopes[slopeIndex][emptyPointIndex][0] = slopes[c][0];// move point to similarSlopes 
    	    				similarSlopes[slopeIndex][emptyPointIndex][1] = slopes[c][1]; 
    	    				similarSlopes[slopeIndex][emptyPointIndex][2] = slopes[c][2];
    	    				
    	    				emptyIndex = 0;
            				while (slopes[emptyIndex][0] != 0){// find lowest empty index in slopes
            					emptyIndex++;
            				}
            				
            				for (int i3=c;i3<emptyIndex;i3++){// remove point from slopes
            					slopes[i3][0] = slopes[i3+1][0];
            					slopes[i3][1] = slopes[i3+1][1];
            					slopes[i3][2] = slopes[i3+1][2];
            				}
    						
    						break;
    					}
    				}else{
    					slopeIndex = 1;// if emptySlopeIndex = 0 then the first new slope index must be 1
    					checkSlope = (float) (slopes[c][0]);
    					System.out.println("First new slope index");
    					emptyPointIndex = 0;
	    				while (similarSlopes[slopeIndex][emptyPointIndex][0] != 0){// find lowest empty point index in similarSlopes in slope index 0
	    					if (emptyPointIndex < similarSlopes[slopeIndex].length){
								emptyPointIndex++;
							}else{
								break;
							}
	    				}
						similarSlopes[slopeIndex][emptyPointIndex][0] = slopes[c][0];// move point to similarSlopes 
	    				similarSlopes[slopeIndex][emptyPointIndex][1] = slopes[c][1]; 
	    				similarSlopes[slopeIndex][emptyPointIndex][2] = slopes[c][2];
        				
        				emptyIndex = 0;
        				while (slopes[emptyIndex][0] != 0){// find lowest empty index in slopes
        					emptyIndex++;
        				}
        				
        				for (int i3=c;i3<emptyIndex;i3++){// remove point from slopes
        					slopes[i3][0] = slopes[i3+1][0];
        					slopes[i3][1] = slopes[i3+1][1];
        					slopes[i3][2] = slopes[i3+1][2];
        				}
        				
    				}
    			}
        	}
        	
			
			
			emptySlopeIndex = 0;
			while (similarSlopes[emptySlopeIndex][0][0] != 0){// find lowest empty slope index in similarSlopes
				emptySlopeIndex++;
			}
			
			emptyPointIndex = 0;
			while (similarSlopes[0][emptyPointIndex][0] != 0){// find lowest empty point index in similarSlopes in slope index 0
				if (emptyPointIndex < similarSlopes[0][0].length){
					emptyPointIndex++;
				}else{
					break;
				}
			}
			
			
			
			
			emptyIndex = 0;
			while (slopes[emptyIndex][0] != 0){// find lowest empty index in slopes
				emptyIndex++;
			}
			if (emptyIndex == 0){// exit while loop if there are no more slopes to sort
				break;
			}
			epoch++;
		}
        
        
        emptySlopeIndex = 0;
		while (similarSlopes[emptySlopeIndex][0][0] != 0){// find lowest empty slope index in similarSlopes
			emptySlopeIndex++;
		}
		
		System.out.println(outAmount+emptySlopeIndex);
        return outAmount+emptySlopeIndex;
	}
	
	
}
