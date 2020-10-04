
public class SATCollisionDetection {
	private static CollisionInfo collisionInfo;
	
	public static boolean detectCollision(Object b1, Object b2) {
		
		if(b1 == null || b2 == null) {
			return false;
		}
		
		if(b1.hasCollision == false || b2.hasCollision == false) {
			return false;
		}
		
		//
		// aabb overlap test
		//

		if (!(0 > Math.abs(b2.boundingBoxCenter.x - b1.boundingBoxCenter.x) - (b2.boundingBoxHalfEx.x + b1.boundingBoxHalfEx.x) 
				&& 0 > Math.abs(b2.boundingBoxCenter.y - b1.boundingBoxCenter.y) - (b2.boundingBoxHalfEx.y + b1.boundingBoxHalfEx.y))) {
			return false; // no aabb overlap
		}
		
		//
		// SAT collision detection
		//
		
		collisionInfo = new CollisionInfo();
		
		float minDistance = 100000000.0f;
		
		for(int i=0; i < b1.edgeCount + b2.edgeCount; i++) {
			Edge e;
			
			if(i < b1.edgeCount) {
				e = b1.edges[i];
				collisionInfo.e_object = b1;
				collisionInfo.v_object = b2;
			}else {
				e = b2.edges[i - b1.edgeCount];
				collisionInfo.e_object = b2;
				collisionInfo.v_object = b1;
			}
			
		/*
		 * Calculate the axis perpendicular to this edge and normalize it
		 */
			Vec2 axis = new Vec2(e.v1.position.y - e.v2.position.y, e.v2.position.x - e.v1.position.x);
			if(axis.normalize() == false) {
				return false;
			}
			
		/*
		 * Project both bodies onto the perpendicular axis
		 */
			Vec2 minmaxA = new Vec2(0f,0f);
			Vec2 minmaxB = new Vec2(0f,0f);
			
			minmaxA = b1.projectToAxis(axis, minmaxA);
			minmaxB = b2.projectToAxis(axis, minmaxB);
			
		/*
		 * Calculate the distance between the two intervals
		 */
			float distance = intervalDistance(minmaxA, minmaxB);
		/*
		 * If the intervals don't overlap, return, since there is no collision
		 */
			if(distance > 0.0f) {
				return false;
			}else if( Math.abs(distance) < minDistance) {
				minDistance = Math.abs(distance);
				collisionInfo.normal = axis;//Save collision information for later
				collisionInfo.e = e; //Store the edge, as it is the collision edge
			}
		}
		
		collisionInfo.depth = minDistance;
	/*
	 * Make sure Collision edge is b2
	 * Make sure Vertex is b1
	 */
		
		if(collisionInfo.e.parent != b2) {
			Object temp = b2;
			b2 = b1;
			b1 = temp;
			
			collisionInfo.e_object = b2;
			collisionInfo.v_object = b1;
		}
		
	/*
	 * This is needed to make sure that the collision normal is pointing at B1
	 */
		Vec2 centerVec2 = new Vec2(b1.getCenter().x - b2.getCenter().x, b1.getCenter().y - b2.getCenter().y);
		if(collisionInfo.normal.dot(centerVec2) < 0f) {
			collisionInfo.normal.x = -collisionInfo.normal.x;
			collisionInfo.normal.y = -collisionInfo.normal.y;
		}
		
		float smallestD = 100000000.0f;//Smallest distance
		
		for(int i=0; i < b1.vertexCount; i++) {
		/*
		 * Measure the distance of the vertex from the line using the line equation
		 */
			Vec2 posCenterVec2 = new Vec2(b1.vertices[i].position.x - b2.getCenter().x, b1.vertices[i].position.y - b2.getCenter().y);
			float distance = collisionInfo.normal.dot(posCenterVec2);
		
		/*
		 * If the measured distance is smaller than the smallest distance reported
		 * so far, set the smallest distance and the collision vertex
		 * Point closest to the center of b2 in the normal (axis) direction
		 */
			if(distance < smallestD) {
				smallestD = distance;
				collisionInfo.v = b1.vertices[i];
			}
		}
	/*
	 * There is no separating axis. Report a collision!
	 */
		return true;
	}
	
	public static void processCollision() {
		float m0;
		float m1;
		float lambda;
		float t;
		
		/*
		 * Process Collision
		 */
			Vec2 collisionVector = new Vec2(collisionInfo.normal.x * collisionInfo.depth, collisionInfo.normal.y * collisionInfo.depth);
			/*
			 * collision edge Verticies
			 */
				Vertex e_v1 = collisionInfo.e.v1;
				Vertex e_v2 = collisionInfo.e.v2;
				
			/*
			 * Checks to make sure we dont divide by zero
			 */
				if(Math.abs(e_v1.position.x - e_v2.position.x) > Math.abs(e_v1.position.y - e_v2.position.y)) {
					t = (collisionInfo.v.position.x - collisionVector.x - e_v1.position.x )/( e_v2.position.x - e_v1.position.x);
				}else {
					t = (collisionInfo.v.position.y - collisionVector.y - e_v1.position.y )/( e_v2.position.y - e_v1.position.y);
				}
				
			/*
			 * push the collision edge apart by the collision vector
			 */
				
				lambda = 1.0f/(t*t + (1.0f-t)*(1.0f-t));
				
			/*
			 * Mass Coefficient
			 */
				
				m0 = collisionInfo.v_object.mass;
				m1 = collisionInfo.e_object.mass;
				float tm = m0 + m1;
				collisionInfo.v_object_m = m0/tm;
				collisionInfo.e_object_m = m1/tm;
				
			/*
			 * CORRECT EDGE WITH COLLISION VECTOR
			 */
				
				e_v1.position.x -= collisionVector.x * (1.0f-t) * 0.5f * lambda * collisionInfo.v_object_m;
				e_v1.position.y -= collisionVector.y * (1.0f-t) * 0.5f * lambda * collisionInfo.v_object_m;
				
				e_v2.position.x -= collisionVector.x * t * 0.5f * lambda * collisionInfo.v_object_m;
				e_v2.position.y -= collisionVector.y * t * 0.5f * lambda * collisionInfo.v_object_m;
			
			/*
			 * CORRECT VERTEX WITH COLLISION VECTOR
			 */
				
				collisionInfo.v.position.x += collisionVector.x * 0.5f * collisionInfo.e_object_m;
				collisionInfo.v.position.y += collisionVector.y * 0.5f * collisionInfo.e_object_m;
				
			/*
			 * COLLISION FRICTION
			 */
				/*
				 * EDGE FRICTION
				 */
				
				e_v1.position.x -= (e_v1.position.x - e_v1.oldPosition.x) * e_v1.parent.collisionFriction * 0.25f;
				e_v1.position.y -= (e_v1.position.x - e_v1.oldPosition.x) * e_v1.parent.collisionFriction * 0.25f;
				
				e_v2.position.x -= (e_v2.position.x - e_v2.oldPosition.x) * e_v2.parent.collisionFriction * 0.25f;
				e_v2.position.y -= (e_v2.position.x - e_v2.oldPosition.x) * e_v2.parent.collisionFriction * 0.25f;
				
				/*
				 * VERTEX FRICTION
				 */
				
				collisionInfo.v.position.x -= (collisionInfo.v.position.x - collisionInfo.v.oldPosition.x) * collisionInfo.v.parent.collisionFriction * 0.5f;
				collisionInfo.v.position.y -= (collisionInfo.v.position.y - collisionInfo.v.oldPosition.y) * collisionInfo.v.parent.collisionFriction * 0.5f;
		
		/*
		 * Send CollisionInfo to objects
		 *
		collisionInfo.v_object.collisionInfo.depth = collisionInfo.depth;
		collisionInfo.v_object.collisionInfo.normal = collisionInfo.normal;
		collisionInfo.v_object.collisionInfo.e = collisionInfo.e;
		collisionInfo.v_object.collisionInfo.e_object = collisionInfo.e_object;
		collisionInfo.v_object.collisionInfo.v = collisionInfo.v;
		collisionInfo.v_object.collisionInfo.v_object = collisionInfo.v_object;
		collisionInfo.v_object.collisionInfo.collisionVector = collisionVector;
		
		collisionInfo.e_object.collisionInfo.depth = collisionInfo.depth;
		collisionInfo.e_object.collisionInfo.normal = collisionInfo.normal;
		collisionInfo.e_object.collisionInfo.e = collisionInfo.e;
		collisionInfo.e_object.collisionInfo.e_object = collisionInfo.e_object;
		collisionInfo.e_object.collisionInfo.v = collisionInfo.v;
		collisionInfo.e_object.collisionInfo.v_object = collisionInfo.v_object;
		collisionInfo.e_object.collisionInfo.collisionVector = collisionVector;
		*/
	}
	
	
public static boolean mouseCollision(Vec2 mousePos, Object b1) {
		
		for(int i=0; i < b1.edgeCount; i++) {
			
			Edge e = b1.edges[i];
			
		/*
		 * Calculate the axis perpendicular to this edge and normalize it
		 */
			Vec2 axis = new Vec2(e.v1.position.y - e.v2.position.y, e.v2.position.x - e.v1.position.x);
			if(axis.normalize() == false) {
				return false;
			}
			
		/*
		 * Project both bodies onto the perpendicular axis
		 */
			Vec2 minmax = new Vec2(0f,0f);
			float mouseDot;
			
			minmax = b1.projectToAxis(axis, minmax);
			mouseDot = mousePos.dot(axis);
			
			
		/*
		 * Check if mouse is inside all edge projections
		 */
			
			if(mouseDot < minmax.x || mouseDot > minmax.y) {
				return false;
			}
		}
	/*
	 * There is no separating axis. Report a collision!
	 */
		return true;
	}
	
	
	private static float intervalDistance(Vec2 minmaxA, Vec2 minmaxB) {
		if(minmaxA.x < minmaxB.x) {
			return minmaxB.x - minmaxA.y;
		}else {
			return minmaxA.x - minmaxB.y;
		}
	}
}
