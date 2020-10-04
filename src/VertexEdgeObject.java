
public class VertexEdgeObject {	
	Vertex[] vertices;
	int vertexCount = 0;
	Edge[] edges;
	int edgeCount = 0;
	Constraint[] constraints;
	int constraintCount = 0;
	
	VertexEdgeObject(){
		vertices = new Vertex[Entities.MAX_VERTICES];
		edges = new Edge[Entities.MAX_EDGES];
		constraints = new Constraint[Entities.MAX_CONSTRAINTS];
	}
	
	public void addVertex(Vertex v) {
		vertices[vertexCount] = v;
		vertexCount++;
	}
	public void addEdge(Edge e) {
		edges[edgeCount] = e;
		edgeCount++;
	}
	public void addConstraint(Constraint e) {
		constraints[constraintCount] = e;
		constraintCount++;
	}
}
