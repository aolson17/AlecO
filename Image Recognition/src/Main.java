import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Main{
	
    JTextField userInput = new JTextField(10);
	
    int width;
    int height;
    int output = 0;
    float imageArea = 0;
    float imageSides = 0;
    String judgement = "?";
    float judgeDifference = 0;
    
    static ImageCharacteristics IC;
    static DrawArea DA; 
    
	JButton clearBtn, teachBtn, judgeBtn; 
    ActionListener actionListener = new ActionListener() {
    	
    	public void actionPerformed(ActionEvent e) {
        	if (e.getSource() == clearBtn){
        		DA.clear();
        	}else if (e.getSource() == teachBtn){
        		//DrawArea drawArea = new DrawArea();
        		//drawArea.teach();
        		teach();	
        	}else if (e.getSource() == judgeBtn){
        		//DrawArea drawArea = new DrawArea();
        		//drawArea.teach();
        		Color c = new Color(DA.imageDrawn.getRGB(3, 0), false);
	            System.out.println("c.getBlue(): "+c.getBlue());
        		judge(DA.imageDrawn);	
        	}
    	}
    };
    
	static public void main(String args[]) throws Exception{
		DA = new DrawArea();
		
		//methods.List();	
		new Main().show();
		IC = new ImageCharacteristics(DA);
		Methods methods = new Methods(IC);
		methods.Review();
	}
	
	public void show() {
		JFrame frame = new JFrame("Paint");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		//final DrawArea drawArea = new DrawArea();
		//drawArea = new DrawArea();
		content.add(DA, BorderLayout.CENTER);
		JPanel controls = new JPanel();
		JPanel textInput = new JPanel();
		
		
		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(actionListener);
		teachBtn = new JButton("Teach");
		teachBtn.addActionListener(actionListener);
		judgeBtn = new JButton("Judge");
		judgeBtn.addActionListener(actionListener);
		
		
		controls.add(clearBtn);
		controls.add(teachBtn);
		controls.add(judgeBtn);
		textInput.add(userInput);
		
		content.add(controls, BorderLayout.NORTH);
		content.add(textInput, BorderLayout.SOUTH);
		
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		
		//String input = "";
		//System.out.println(input);
	}
	public void teach() {
		//Main main = new Main();
		String title = userInput.getText();
		System.out.println(title);
		System.out.println("taught");
		DrawArea drawArea = new DrawArea();
		drawArea.save(title);
		
	}
	public void judge(BufferedImage imageToJudge) {
		String title = userInput.getText();
		System.out.println(title);
		System.out.println("judge");
		
        imageArea = IC.imageEvaluateArea(imageToJudge);

        imageSides = IC.imageEvaluateSides(imageToJudge);
        
        //System.out.println("outAmount " + outAmount);
        judgeDifference = 0;
        
        for (int i = 0; i<Methods.taught.size();i++){
        	
        	String[] splitTitle = Methods.taught.get(i).split("\\s");
        	
        	//System.out.println("current " + judgeDifference);
        	//System.out.println("likeness " + Math.abs(outAmount - Float.parseFloat(splitTitle[1])));
        	
        	if ((float)((Float.parseFloat(splitTitle[1]) / imageArea)+(Float.parseFloat(splitTitle[2]) / imageSides))/2 > judgeDifference || judgeDifference == 0){
        		judgeDifference = (float)((Float.parseFloat(splitTitle[1]) / imageArea)+(Float.parseFloat(splitTitle[2]) / imageSides))/2;
        		judgement = splitTitle[0];
        		System.out.println("judgement: " + judgement);
        		System.out.println("judgeDifference: " + judgeDifference);
        	}
			
		}
        
        System.out.println(judgement);
        System.out.println("Area: "+imageArea);
        System.out.println("Sides: "+imageSides);
		
	}
	
	
	
	
}