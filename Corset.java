import java.util.*;
import com.jogamp.opengl.util.*;
import javax.media.opengl.*;


public class Corset{

  public void provideCircle(int numberOfCircle, CorsetCircle circle){
    circles[numberOfCircle + 1] = circle;
  }
  
  public Corset(int numberOfCircles, int numberOfSidePieces, String textureName, String fragmentShaderName){
    this.numberOfSidePieces = numberOfSidePieces;
    this.numberOfCircles = numberOfCircles + 2;
    this.textureName = textureName;
    this.fragmentShaderName = fragmentShaderName;
    circles = new CorsetCircle[numberOfCircles + 2];
    patterns = new Pattern[2*numberOfSidePieces + 5];
  }
  
  public void generatePatterns(){
    circles[0] = circles[1].clone();
    circles[numberOfCircles-1] = circles[numberOfCircles-2].clone();
  
    findVertexes();
    
    for(int l = 0; l<patterns.length; ++l)
	patterns[l] = new Pattern();
	  
    for(int i = 0; i<circles.length -1; ++i){
      for(int j = 0; j<patterns.length; ++j){
      
	if(true){	//TODO
	  patterns[j].addPatternPiece(
	    new PatternPiece(
	      circles[i].getVertex(j),
	      circles[i+1].getVertex(j+1),
	      circles[i].getVertex(j+1)
	    ), true
	  );
	  patterns[j].addPatternPiece(
	    new PatternPiece(
	      circles[i].getVertex(j), 
	      circles[i+1].getVertex(j),
	      circles[i+1].getVertex(j+1)
	    ), false
	  );
	} else{
	  
	}
      }
    }
    
    this.corsetViewer = new CorsetViewer(patterns, this, textureName, fragmentShaderName);	//TODO
  }
  
  private void findVertexes(){
    for(CorsetCircle circle: circles)
      findVertexes(circle);
  }
  
  private void findVertexes(CorsetCircle circle){
    double scale = 20;
    int sign = -1;
    double step = 3;
    
    double solutionValue = circle.createSolution(scale);
    
    while(solutionValue > 0.01){
      scale += sign*step;
      double newSolutionValue = circle.createSolution(scale);
      if(newSolutionValue > solutionValue){
	sign *= -1;
	step /=2;
      }
      
      solutionValue = newSolutionValue;
    }
  }
  
  public void addTexture(GL2 gl, String textureName){
  
    patterns[0].addTexture(gl, textureName + "/front_right.png");
    patterns[numberOfSidePieces + 1].addTexture(gl, textureName + "/back_right.png");
    patterns[numberOfSidePieces + 2].addTexture(gl, textureName + "/space.png");
    patterns[numberOfSidePieces + 3].addTexture(gl, textureName + "/back_left.png");
    patterns[2*numberOfSidePieces + 4].addTexture(gl, textureName + "/front_left.png");
    
    for(int i = 0; i< numberOfSidePieces; ++i){
      patterns[i+1].addTexture(gl, textureName + "/side_" + i + "_right.png");
      patterns[2*numberOfSidePieces + 3 - i].addTexture(gl, textureName + "/side_" + i + "_left.png");
    }
    
  }
  
  public static void main(String[] args){
  
    int numberOfSidePieces = 6;
    
    if(args.length != 1 && args.length != 2){
      System.err.println("Please use following call:\n\tjava -cp \"./jars/*:.\" Corset <texture name> [<fragment shader name>]");
      System.exit(-1);
    } 
  
    String fragmentShaderName = null;
    if(args.length == 2)
      fragmentShaderName = args[1];
    
    Corset corset = new Corset(6, numberOfSidePieces, args[0], fragmentShaderName);
    
    /*corset.provideCircle(0, new CorsetCircle(	0, 	1.3, 	95, 	10, 		12, 	3, numberOfSidePieces));
    corset.provideCircle(1, new CorsetCircle(	4, 	1.3, 	92, 	12, 		13, 	3, numberOfSidePieces));
    corset.provideCircle(2, new CorsetCircle(	7, 	1.4, 	84, 	13.5, 		13.75, 	3, numberOfSidePieces));
    corset.provideCircle(3, new CorsetCircle(	16, 	1.3, 	70, 	18, 		16, 	3, numberOfSidePieces));
    corset.provideCircle(4, new CorsetCircle(	24, 	1.2, 	73, 	22, 		20, 	3, numberOfSidePieces));
    corset.provideCircle(5, new BustCircle(	31, 	1, 	88, 	25, 	44,	22, 	3, numberOfSidePieces));
    */
    
    corset.provideCircle(0, new CorsetCircle(	0, 	1.3, 	95, 	10, 			12, 	3, numberOfSidePieces));
    corset.provideCircle(1, new CorsetCircle(	4, 	1.3, 	92, 	10, 			12, 	3, numberOfSidePieces));
    corset.provideCircle(2, new CorsetCircle(	7, 	1.4, 	84, 	10, 			12, 	3, numberOfSidePieces));
    corset.provideCircle(3, new CorsetCircle(	16, 	1.3, 	70, 	11, 			13, 	3, numberOfSidePieces));
    corset.provideCircle(4, new CorsetCircle(	24, 	1.2, 	73, 	13, 			15, 	3, numberOfSidePieces));
    corset.provideCircle(5, new BustCircle(	31, 	1.1, 	88, 	15, 	44,	2,	18, 	3, numberOfSidePieces));
    
    /*corset.provideCircle(0, new CorsetCircle(0, 1.3, 95, 22, 12, 3, numberOfSidePieces));
    corset.provideCircle(1, new CorsetCircle(4, 1.3, 92, 22, 13, 3, numberOfSidePieces));
    corset.provideCircle(2, new CorsetCircle(7, 1.4, 84, 20, 13.75, 3, numberOfSidePieces));
    corset.provideCircle(3, new CorsetCircle(16, 1.3, 70, 16, 16, 3, numberOfSidePieces));
    corset.provideCircle(4, new CorsetCircle(24, 1.2, 73, 22, 20, 3, numberOfSidePieces));
    */
    corset.generatePatterns();
   
  }

  private int numberOfSidePieces;
  private int numberOfCircles;
  private CorsetCircle[] circles;
  private Pattern[] patterns;
  private CorsetViewer corsetViewer;
  private String textureName;
  private String fragmentShaderName;
}
