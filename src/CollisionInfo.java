
class CollisionInfo{
	public float depth;
	public Vec2 normal;
	public Edge e;
	public Object e_object;
	public Vertex v;
	public Object v_object;
	public float e_object_m;
	public float v_object_m;
	
	public Vec2 collisionVector; 
	
	public CollisionInfo() {
		depth = 0f;
		normal = new Vec2(0f,0f);
	}
	
}
