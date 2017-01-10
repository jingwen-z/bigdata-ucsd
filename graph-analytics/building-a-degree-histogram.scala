// Week 5 - Computing Platforms for Graph Analytics
//
// Hands On: Building A Degree Histogram

/** count the number of vertices and edges */
metrosGraph.numEdges
metrosGraph.numVertices

/** define a min and max function for Spark’s reduce method */
def max(a: (VertexId, Int), b: (VertexId, Int)): (VertexId, Int) = {
  if (a._2 > b._2) a else b
}

def min(a: (VertexId, Int), b: (VertexId, Int)): (VertexId, Int) = {
  if (a._2 <= b._2) a else b
}

/** compute min and max degrees */

// find which VertexId and the edge count of the vertex with the most out edges
// this can be any vertex because all vertices have one out edge
metrosGraph.outDegrees.reduce(max)
metrosGraph.vertices.filter(_._1 == 5).collect()

// find which which VertexId and the edge count of the vertex 
// with the most in edges
metrosGraph.inDegrees.reduce(max)
metrosGraph.vertices.filter(_._1 == 108).collect()

metrosGraph.degrees.reduce(max)
metrosGraph.degrees.reduce(min)

// find the number vertexes that have only one out edge
metrosGraph.outDegrees.filter(_._2 <= 1).count

/** compute the histogram data of the degree of connectedness */
metrosGraph.degrees.
  filter { case (vid, count) => vid >= 100 }. 
  // Apply filter so only VertexId < 100(countries) are included
  map(t => (t._2,t._1)).
  groupByKey.map(t => (t._1,t._2.size))
  sortBy(_._1).collect()
