import java.util.*;
import java.util.ArrayList;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.texture.*;
import javax.media.opengl.*;

public class PatternPiece{

  public PatternPiece(CorsetVertex a, CorsetVertex b, CorsetVertex c){
    this.a = a;
    this.b = b;
    this.c = c;
    textureCoordinates = new Map.Entry[3];
  }
  
  public CorsetVertex getA(){
    return a;
  }
  
  public CorsetVertex getB(){
    return b;
  }
  
  public CorsetVertex getC(){
    return c;
  }
  
  public void addTextureCoordinates(Map.Entry<Double, Double> a, Map.Entry<Double, Double> b, Map.Entry<Double, Double> c){
    textureCoordinates[0] = a;
    textureCoordinates[1] = b;
    textureCoordinates[2] = c;
  }
  
  public Map.Entry<Double, Double> getTextureA(){
    return (Map.Entry<Double, Double>)textureCoordinates[0];
  }
  
  public Map.Entry<Double, Double> getTextureB(){
    return (Map.Entry<Double, Double>)textureCoordinates[1];
  }
  
  public Map.Entry<Double, Double> getTextureC(){
    return (Map.Entry<Double, Double>)textureCoordinates[2];
  }
  
  public CorsetVertex getNormalVector(){
    CorsetVertex ab = new CorsetVertex(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    CorsetVertex bc = new CorsetVertex(b.getX() - c.getX(), b.getY() - c.getY(), b.getZ() - c.getZ());
    return new CorsetVertex(ab.getY()*bc.getZ() - ab.getZ()*bc.getY(), ab.getZ()*bc.getX() - ab.getX()*bc.getZ(), ab.getX()*bc.getY() - ab.getY()*bc.getX());
  }
  
  public double getAngleBetweenPatternPieces(PatternPiece piece){
    CorsetVertex vectorA = this.getNormalVector();
    CorsetVertex vectorB = piece.getNormalVector();
    
    double cosinus = (vectorA.getX()*vectorB.getX() + vectorA.getY()*vectorB.getY() + vectorA.getZ()*vectorB.getZ())/(vectorA.getLength()*vectorB.getLength());
    
    int sign = 1;
    
    if(vectorA.getY() > 0)
      sign = -1;
    
    return sign*Math.acos(cosinus);
  }
  
  public CorsetVertex[] getFlattingMatrix(PatternPiece basePiece){
    
    double angle = getAngleBetweenPatternPieces(basePiece);
    double cosinus = Math.cos(angle);
    double sinus = Math.sin(angle);
    
    CorsetVertex axis = new CorsetVertex(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    double length = axis.getLength();
    
    axis.setX(axis.getX()/length);
    axis.setY(axis.getY()/length);
    axis.setZ(axis.getZ()/length);
    
    CorsetVertex[] matrixRows = new CorsetVertex[3];
    
    matrixRows[0] = new CorsetVertex(0, 0, 0);
    matrixRows[0].setX(cosinus + Math.pow(axis.getX(), 2)*(1-cosinus));
    matrixRows[0].setY(axis.getX()*axis.getY()*(1-cosinus) - axis.getZ()*sinus);
    matrixRows[0].setZ(axis.getX()*axis.getZ()*(1-cosinus) + axis.getY()*sinus);
    
    matrixRows[1] = new CorsetVertex(0, 0, 0);
    matrixRows[1].setX(axis.getX()*axis.getY()*(1-cosinus) + axis.getZ()*sinus);
    matrixRows[1].setY(cosinus + Math.pow(axis.getY(), 2)*(1-cosinus));
    matrixRows[1].setZ(axis.getZ()*axis.getY()*(1-cosinus) - axis.getX()*sinus);
    
    matrixRows[2] = new CorsetVertex(0, 0, 0);
    matrixRows[2].setX(axis.getX()*axis.getZ()*(1-cosinus) - axis.getY()*sinus);
    matrixRows[2].setY(axis.getZ()*axis.getY()*(1-cosinus) + axis.getX()*sinus);
    matrixRows[2].setZ(cosinus + Math.pow(axis.getZ(), 2)*(1-cosinus));
    
    return matrixRows;
    
  }
  
  public void draw(GL2 gl, Texture texture){
    //gl.glColor3f(0, 0, 1);
    
	    
	    gl.glTexCoord2f(
	      (float)(getTextureA().getKey().doubleValue()), 
	      (float)(getTextureA().getValue().doubleValue()));
	    gl.glVertex3d(
	      a.getX(), 
	      a.getZ(), 
	      a.getY());
	    gl.glTexCoord2f(
	      (float)(getTextureB().getKey().doubleValue()), 
	      (float)(getTextureB().getValue().doubleValue()));
	    gl.glVertex3d(
	      b.getX(), 
	      b.getZ(), 
	      b.getY());
	    gl.glTexCoord2f(
	      (float)(getTextureC().getKey().doubleValue()), 
	      (float)(getTextureC().getValue().doubleValue()));
	    gl.glVertex3d(
	      c.getX(), 
	      c.getZ(), 
	      c.getY());
  }
  
  public String toString(){
    return a.toString() + " " + b.toString() + " " + c.toString();
  }

  private CorsetVertex a;
  private CorsetVertex b;
  private CorsetVertex c;
  private Map.Entry[] textureCoordinates;
}
