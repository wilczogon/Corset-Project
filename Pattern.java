import java.util.*;
import com.jogamp.opengl.util.*;
import javax.media.opengl.*;
import com.jogamp.opengl.util.texture.*;
import java.io.*;
import java.lang.*;

public class Pattern{

  public Pattern(){
    pieces = new ArrayList<PatternPiece>();
    flatPieces = new ArrayList<PatternPiece>();
    points = new ArrayList<Map.Entry<Double, Double>>();
  }
  
  public void addTexture(GL2 gl, String textureFile){
    double rightSum = 0;
    double leftSum = 0;
    for(int i = 0; i<pieces.size(); ++i){
      if(i%2 == 0){
	double height = pieces.get(i).getB().getDistance(pieces.get(i).getC());
	pieces.get(i).addTextureCoordinates(
	  new AbstractMap.SimpleEntry<Double, Double>(new Double((double)0.01), leftSum/leftLength), 
	  new AbstractMap.SimpleEntry<Double, Double>(new Double((double)0.99), (rightSum+height)/rightLength), 
	  new AbstractMap.SimpleEntry<Double, Double>(new Double((double)0.99), rightSum/rightLength)
	);
	rightSum += height;
      } else{
	double height = pieces.get(i).getA().getDistance(pieces.get(i).getB());
	pieces.get(i).addTextureCoordinates(
	  new AbstractMap.SimpleEntry<Double, Double>(new Double((double)0.01), leftSum/leftLength), 
	  new AbstractMap.SimpleEntry<Double, Double>(new Double((double)0.01), (leftSum+height)/leftLength), 
	  new AbstractMap.SimpleEntry<Double, Double>(new Double((double)0.99), rightSum/rightLength)
	);
	leftSum += height;
      }
    }
    
    try{
      texture = TextureIO.newTexture(new File(textureFile), false);
    } catch(IOException e){
      System.err.println("Exception while loading texture '" + textureFile + "' - " + e.toString());
      System.err.println("Probably incorrect format of texture.");
    }
  }
  
  public void addPatternPiece(PatternPiece piece, boolean lower){
    pieces.add(piece);
    flatPieces.add(new PatternPiece(piece.getA(), piece.getB(), piece.getC()));
    
    if(lower)
      rightLength += piece.getB().getDistance(piece.getC());
    else
      leftLength += piece.getA().getDistance(piece.getB());
  }
  
  public List<PatternPiece> getPatternPieces(){
    return pieces;
  }
  
  public void generatePoints(){
    for(int i = 0; i< flatPieces.size() -1; ++i){
      CorsetVertex[] matrix = flatPieces.get(i+1).getFlattingMatrix(flatPieces.get(i));
      
      flatPieces.get(i+1).getC().multipleByMatrix(matrix);
      for(int j = i+2; j< flatPieces.size(); ++j){
	flatPieces.get(j).getA().multipleByMatrix(matrix);
	flatPieces.get(j).getB().multipleByMatrix(matrix);
	flatPieces.get(j).getC().multipleByMatrix(matrix);
      }
    }
  }
  
  public int getNumberOfPoints(){
    return points.size();
  }
  
  public Map.Entry<Double, Double> getPoint(int index){
    return points.get(index);
  }
  
  public void draw(GL2 gl){
  
    gl.glEnable(GL.GL_TEXTURE_2D);
    texture.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
    texture.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
    texture.bind(gl);
    
    gl.glBegin(GL.GL_TRIANGLES);
    
    for(int j = 0; j<pieces.size(); ++j)
	    pieces.get(j).draw(gl, texture);
	    
    gl.glEnd();
  }

  private List<PatternPiece> pieces;
  private List<PatternPiece> flatPieces;
  private List<Map.Entry<Double, Double>> points;
  private Texture texture;
  private int rightLength = 0;
  private int leftLength = 0;
} 
