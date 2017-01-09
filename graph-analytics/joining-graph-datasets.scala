// Week 5 - Computing Platforms for Graph Analytics
//
// Hands On: Joining Graph Datasets

/** create a new dataset */
import org.apache.log4j.Logger
import org.apache.log4j.Level

Logger.getLogger("org").setLevel(Level.ERROR)
Logger.getLogger("akka").setLevel(Level.ERROR)

// import the GraphX and RDD libraries
import org.apache.spark.graphx._
import org.apache.spark.rdd._

// define a simple list of vertices containing five international airports
val airports: RDD[(VertexId, String)] = sc.parallelize(
  List((1L, "Los Angeles International Airport"),
    (2L, "Narita International Airport"),
    (3L, "Singapore Changi Airport"),
    (4L, "Charles de Gaulle Airport"),
    (5L, "Toronto Pearson International Airport")))

// assign a made up flight number to each flight
val flights: RDD[Edge[String]] = sc.parallelize(
  List(Edge(1L,4L,"AA1123"),
    Edge(2L, 4L, "JL5427"),
    Edge(3L, 5L, "SQ9338"),
    Edge(1L, 5L, "AA6653"),
    Edge(3L, 4L, "SQ4521")))

val flightGraph = Graph(airports, flights)

// each triplet in the flightGraph graph represents a flight between two airports
// print the departing and arrival airport and the flight number for each triplet
// in the flightGraph graph
flightGraph.triplets.foreach(t => println("Departs from: " + t.srcAttr + 
  " - Arrives at: " + t.dstAttr + " - Flight Number: " + t.attr))

// define an AirportInformation class to store the airport city and code
case class AirportInformation(city: String, code: String)

// define the list of airport information vertices
val airportInformation: RDD[(VertexId, AirportInformation)] = sc.parallelize(
  List((2L, AirportInformation("Tokyo", "NRT")),
    (3L, AirportInformation("Singapore", "SIN")),
    (4L, AirportInformation("Paris", "CDG")),
    (5L, AirportInformation("Toronto", "YYZ")),
    (6L, AirportInformation("London", "LHR")),
    (7L, AirportInformation("Hong Kong", "HKG"))))

/** join two datasets with JoinVertices */

// create a mapping function that appends the city name to the name of the airport
// the mapping function should return a string since that is the vertex attribute
// type of the flightsGraph graph
def appendAirportInformation(id: VertexId,
                             name: String,
                             airportInformation: AirportInformation):
  String = name + ":"+ airportInformation.city

// use joinVertices on flightGraph to join the airportInformation vertices to a 
// new graph called flightJoinedGraph using the appendAirportInformation mapping
// function
val flightJoinedGraph =
  flightGraph.joinVertices(airportInformation)(appendAirportInformation)

flightJoinedGraph.vertices.foreach(println)

/** join two datasets with outerJoinVertices */

// Use outerJoinVertices on flightGraph to join the airportInformation vertices 
// with additional airportInformation to a new graph called flightOuterJoinedGraph
// using the => operator which is just syntactic sugar for creating instances of
// functions
val flightOuterJoinedGraph =
  flightGraph.outerJoinVertices(airportInformation)((_,name, airportInformation) =>
    (name, airportInformation))

flightOuterJoinedGraph.vertices.foreach(println)

// use outerJoinVertices on flightGraph to join the airportInformation vertices 
// with additional airportInformation to a new graph called flightOuterJoinedGraphTwo
// but this time printing 'NA' if there is no additional information.
val flightOuterJoinedGraphTwo =
  flightGraph.outerJoinVertices(airportInformation)((_, name, airportInformation) =>
    (name, airportInformation.getOrElse(AirportInformation("NA","NA"))))

flightOuterJoinedGraphTwo.vertices.foreach(println)

/** create a new return type for the joined vertices */
case class Airport(name: String, city: String, code: String)

val flightOuterJoinedGraphThree =
  flightGraph.outerJoinVertices(airportInformation)((_, name, b) => b match {
    case Some(airportInformation) =>
      Airport(name, airportInformation.city, airportInformation.code)
    case None => Airport(name, "", "")
  })

flightOuterJoinedGraphThree.vertices.foreach(println)
