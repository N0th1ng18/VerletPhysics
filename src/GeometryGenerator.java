
public class GeometryGenerator{

	public static VertexEdgeObject genGeometry(int vertexCount, float edgeLength, float offsetX, float offsetY, int rows, int columns, float width) {
		VertexEdgeObject ve = new VertexEdgeObject();
		
		if(vertexCount == 2) {
			Vertex v1 = new Vertex(0f-(edgeLength * 0.5f)+offsetX + (rows * width), 0f+offsetY + (columns * width), 0f-(edgeLength * 0.5f)+offsetX + (rows * width), 0f+offsetY + (columns * width));
			Vertex v2 = new Vertex((0f + edgeLength)-(edgeLength * 0.5f)+offsetX + (rows * width), 0f+offsetY + (columns * width), (0f + edgeLength)-(edgeLength * 0.5f)+offsetX + (rows * width), 0f+offsetY + (columns * width));
			Edge e1 = new Edge(v1,v2);
			
			ve.addVertex(v1);
			ve.addVertex(v2);
			ve.addEdge(e1);
		}else {
			
			/*
			 * Creation of Border
			 */
				Vertex v1 = new Vertex(0f-(edgeLength * 0.5f)+offsetX + (rows * width), 0f-(edgeLength * 0.5f)+offsetY + (columns * width), 0f-(edgeLength * 0.5f)+offsetX + (rows * width), 0f-(edgeLength * 0.5f)+offsetY + (columns * width));
				ve.addVertex(v1);
				
				for(int i=0; i < vertexCount-1; i++) {
					Vec2 currentVec = new Vec2(edgeLength , 0.0f);
					
					float ang = (float) ((Math.PI*i)/vertexCount)*2.0f;
					currentVec.rotate(ang);
					
					Vertex v2 = new Vertex(v1.position.x + currentVec.x
							, v1.position.y + currentVec.y
							, v1.position.x + currentVec.x
							, v1.position.y + currentVec.y
							);
					ve.addVertex(v2);
					
					Edge e = new Edge(v1, v2);
					ve.addEdge(e);
					
					v1 = v2;
				}
				
				Edge eFinal = new Edge(ve.vertices[vertexCount-1], ve.vertices[0]);
				ve.addEdge(eFinal);
			/*
			 * Supporting Constraints
			 */
				int vertexConstraintCount = ve.vertexCount - 3;
				int k = 0;
				
				for(int i=0; i < ve.vertexCount - 2; i++) {
					for(int j=0; j < vertexConstraintCount; j++) {
						Constraint c = new Constraint(ve.vertices[i], ve.vertices[(i+2)+j]);
						ve.addConstraint(c);
					}
					vertexConstraintCount = vertexConstraintCount - k;
					k = 1;
				}
			
		}
		
		
		
		return ve;
	}
	
	public static VertexEdgeObject genGeometry(int vertexCount, float edgeLength, float offsetX, float offsetY) {
		VertexEdgeObject ve = new VertexEdgeObject();
		
		if(vertexCount == 2) {
			Vertex v1 = new Vertex(0f-(edgeLength * 0.5f)+offsetX, 0f+offsetY, 0f-(edgeLength * 0.5f)+offsetX, 0f+offsetY);
			Vertex v2 = new Vertex((0f + edgeLength)-(edgeLength * 0.5f)+offsetX, 0f+offsetY, (0f + edgeLength)-(edgeLength * 0.5f)+offsetX, 0f+offsetY);
			Edge e1 = new Edge(v1,v2);
			
			ve.addVertex(v1);
			ve.addVertex(v2);
			ve.addEdge(e1);
		}else {
			
			/*
			 * Creation of Border
			 */
				Vertex v1 = new Vertex(0f-(edgeLength * 0.5f)+offsetX, 0f-(edgeLength * 0.5f)+offsetY, 0f-(edgeLength * 0.5f)+offsetX, 0f-(edgeLength * 0.5f)+offsetY);
				ve.addVertex(v1);
				
				for(int i=0; i < vertexCount-1; i++) {
					Vec2 currentVec = new Vec2(edgeLength , 0.0f);
					
					float ang = (float) ((Math.PI*i)/vertexCount)*2.0f;
					currentVec.rotate(ang);
					
					Vertex v2 = new Vertex(v1.position.x + currentVec.x
							, v1.position.y + currentVec.y
							, v1.position.x + currentVec.x
							, v1.position.y + currentVec.y
							);
					ve.addVertex(v2);
					
					Edge e = new Edge(v1, v2);
					ve.addEdge(e);
					
					v1 = v2;
				}
				
				Edge eFinal = new Edge(ve.vertices[vertexCount-1], ve.vertices[0]);
				ve.addEdge(eFinal);
			/*
			 * Supporting Constraints
			 */
				int vertexConstraintCount = ve.vertexCount - 3;
				int k = 0;
				
				for(int i=0; i < ve.vertexCount - 2; i++) {
					for(int j=0; j < vertexConstraintCount; j++) {
						Constraint c = new Constraint(ve.vertices[i], ve.vertices[(i+2)+j]);
						ve.addConstraint(c);
					}
					vertexConstraintCount = vertexConstraintCount - k;
					k = 1;
				}
			
		}
		
		
		
		return ve;
	}
}
									