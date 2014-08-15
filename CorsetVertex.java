public class CorsetVertex{
  public CorsetVertex(double x, double y, double z){
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public double getX(){
    return x;
  }
  
  public double getY(){
    return y;
  }
  
  public double getZ(){
    return z;
  }
  
  public void setX(double x){
    this.x = x;
  }
  
  public void setY(double y){
    this.y = y;
  }
  
  public void setZ(double z){
    this.z = z;
  }
  
  public double getDistance(CorsetVertex vertex){
    return Math.sqrt(Math.pow(vertex.getX() - this.getX(), 2) +
	Math.pow(vertex.getY() - this.getY(), 2) +
	Math.pow(vertex.getZ() - this.getZ(), 2));
  }
  
  public double getLength(){
    return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2) + Math.pow(getZ(), 2));
  }
  
  public String toString(){
    return "(" + x + ", " + y + ", " + z + ")";
  }
  
  public boolean equals(CorsetVertex vertex){
    if(Math.abs(x - vertex.getX()) > 0.001)
      return false;
      
    if(Math.abs(y - vertex.getY()) > 0.001)
      return false;
      
    if(Math.abs(z - vertex.getZ()) > 0.001)
      return false;
      
    return true;
  }
  
  public void multipleByMatrix(CorsetVertex[] matrix){
    double newX = matrix[0].getX()*x + matrix[0].getY()*y + matrix[0].getZ()*z;
    double newY = matrix[1].getX()*x + matrix[1].getY()*y + matrix[1].getZ()*z;
    double newZ = matrix[2].getX()*x + matrix[2].getY()*y + matrix[2].getZ()*z;
    
    x = newX;
    y = newY;
    z = newZ;
  }
  
  private double x;
  private double y;
  private double z;
}
