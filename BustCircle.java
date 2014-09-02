 
public class BustCircle extends CorsetCircle{

  public BustCircle(double height, 
	double frontToSideProportion, 
	double circuit, 
	double frontPieceWidth, 
	double bustPartLength,
	double backSinglePieceWidth,
	double backPieceWidth, 
	double backSpaceWidth,
	int numberOfSidePieces){
      super(height, frontToSideProportion, circuit, frontPieceWidth, backPieceWidth, backSpaceWidth, numberOfSidePieces);
      this.bustPartLength = bustPartLength;
  }
  
  private void countVertexes(){
    vertexes[0].setY(scale);
    countVertex(vertexes[0], vertexes[1], frontPieceWidth/2);
    
    //double backSinglePieceWidth = (circuit - frontPieceWidth - backPieceWidth)/(2*numberOfSidePieces);
    int backSinglePieceNo = (int)((backSinglePieceWidth*numberOfSidePieces - bustPartLength)/backPieceWidth);
    int frontSinglePieceNo = numberOfSidePieces - backSinglePieceNo;
    double frontSinglePieceWidth = bustPartLength/frontSinglePieceNo;
    
    for(int i = 0; i<frontSinglePieceNo; ++i)
      super.countVertex(vertexes[i+1], vertexes[i+2], frontSinglePieceNo);
      
    for(int i = frontSinglePieceNo; i<numberOfSidePieces; ++i)
      super.countVertex(vertexes[i], vertexes[i], backSinglePieceWidth);
      
    super.countVertex(vertexes[numberOfSidePieces+1], vertexes[numberOfSidePieces+2], (backPieceWidth-backSpaceWidth)/2);
    
  }
  
  public BustCircle clone(){
    return new BustCircle(height, frontToSideProportion, circuit, frontPieceWidth, bustPartLength, backSinglePieceWidth, backPieceWidth, backSpaceWidth, numberOfSidePieces);
  }
  
  public String toString(){
    String result = "BustCircle:\n";
    for(CorsetVertex vertex: vertexes)
      result = result + vertex.toString() + "\n";
    
    return result;
  }
  
  private double bustPartLength;
  private double backSinglePieceWidth;
}
