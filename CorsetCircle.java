public class CorsetCircle{

  public CorsetCircle(double height, 
	double frontToSideProportion, 
	double circuit, 
	double frontPieceWidth, 
	double backPieceWidth, 
	double backSpaceWidth,
	int numberOfSidePieces){
      this.height = height;
      this.frontToSideProportion = frontToSideProportion;
      this.circuit = circuit;
      this.frontPieceWidth = frontPieceWidth;
      this.backPieceWidth = backPieceWidth;
      this.backSpaceWidth = backSpaceWidth;
      this.numberOfSidePieces = numberOfSidePieces;
      vertexes = new CorsetVertex[numberOfSidePieces+3];
	
      for(int i = 0; i<vertexes.length; ++i)
	vertexes[i] = new CorsetVertex(0, 0, height);
  }
  
  public CorsetVertex getVertex(int index){
    if(index < numberOfSidePieces+3)
      return vertexes[index];
    else
      return new CorsetVertex(-vertexes[2*numberOfSidePieces + 5-index].getX(), vertexes[2*numberOfSidePieces + 5-index].getY(), vertexes[2*numberOfSidePieces + 5-index].getZ());
  }
  
  public double createSolution(double scale){
    this.scale = scale;
    countVertexes();
    
    return getSolutionValue();
  }
  
  private double getSolutionValue(){
    if(vertexes[numberOfSidePieces+2].getY() < 0)
      return Math.abs(backSpaceWidth - 2*vertexes[numberOfSidePieces+2].getX());
    else
      return 2*frontToSideProportion*scale + vertexes[numberOfSidePieces+2].getY();
  }
  
  protected void countVertexes(){
    vertexes[0].setY(scale);
    countVertex(vertexes[0], vertexes[1], frontPieceWidth/2);
    
    double singlePieceWidth = (circuit - frontPieceWidth - backPieceWidth)/(2*numberOfSidePieces);
    
    for(int i = 0; i<numberOfSidePieces; ++i)
      countVertex(vertexes[i+1], vertexes[i+2], singlePieceWidth);
      
    countVertex(vertexes[numberOfSidePieces+1], vertexes[numberOfSidePieces+2], (backPieceWidth-backSpaceWidth)/2);
    
  }
  
  protected void countVertex(CorsetVertex fixedVertex, CorsetVertex movableVertex, double distance){
    movableVertex.setX(fixedVertex.getX());
    movableVertex.setY(fixedVertex.getY());
    
    double angle = Math.atan(fixedVertex.getX()/fixedVertex.getY());
    
    if(fixedVertex.getX() > 0 && fixedVertex.getY() < 0)
      angle += Math.PI;
    else if(fixedVertex.getX() < 0 && fixedVertex.getY() < 0)
      angle += Math.PI;
    else if(fixedVertex.getX() < 0 && fixedVertex.getY() > 0)
      angle += 2*Math.PI;
    
    int sign = 1;
    double step = Math.PI/50;
    double minAngle = angle;
    double maxAngle = 2*Math.PI;
    
    double solutionValue = fixedVertex.getDistance(movableVertex);
    
    while(Math.abs(solutionValue - distance) > 0.001){
      angle+= sign*step;
      if(angle > maxAngle){
	angle = Math.max(minAngle, angle - Math.random()*10*step);
      } else if (angle < minAngle){
	angle = Math.min(maxAngle, angle + Math.random()*10*step);
      }
      
      double x0 = frontToSideProportion*scale;
      double y0 = scale;
      
      double tmp = y0/Math.sqrt(Math.pow(Math.cos(angle), 2) + Math.pow(Math.sin(angle)*y0/x0, 2));
      
      movableVertex.setX(Math.sin(angle)*tmp);      
      movableVertex.setY(Math.cos(angle)*tmp);

      double newSolutionValue = fixedVertex.getDistance(movableVertex);
      
      if(Math.signum(newSolutionValue - distance) != Math.signum(solutionValue - distance)){
	sign *= -1;
	step /= 2;
      } /*else if((Math.abs(solutionValue - distance) - Math.abs(newSolutionValue - distance))/Math.abs(solutionValue - distance) < 0.1){
	step *= 1.1;
      }*/
      
      solutionValue = newSolutionValue;
    }
  }
  
  public CorsetCircle clone(){
    return new CorsetCircle(height, frontToSideProportion, circuit, frontPieceWidth, backPieceWidth, backSpaceWidth, numberOfSidePieces);
  }
  
  public String toString(){
    String result = "Circle:\n";
    for(CorsetVertex vertex: vertexes)
      result = result + vertex.toString() + "\n";
    
    return result;
  }
  
  protected double height;
  protected double scale;
  protected double frontToSideProportion;
  protected double circuit;
  protected double frontPieceWidth;
  protected double backPieceWidth;
  protected double backSpaceWidth;
  protected int numberOfSidePieces;
  protected CorsetVertex[] vertexes;
}
