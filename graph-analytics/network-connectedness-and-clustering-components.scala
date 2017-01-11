// Week 5 - Computing Platforms for Graph Analytics
//
// Hands On: Network Connectedness and Clustering Components

/** create a new graph by adding the Continents dataset */
Source.fromFile("./EOADATA/continent.csv").getLines().take(5).foreach(println)
Source.fromFile("./EOADATA/country_continent.csv").getLines().take(5).
  foreach(println)

case class Continent(override val name: String) extends PlaceNode(name)

val continents: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/continent.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (200L + row(0).toInt, Continent(row(1)))
      // add 200 to the VertexId to keep the indexes unique
    }

val cclinks: RDD[Edge[Int]] =
  sc.textFile("./EOADATA/country_continent.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      Edge(100L + row(0).toInt, 200L + row(1).toInt, 1)
    }

val cnodes = metros ++ countries ++ continents
val clinks = mclinks ++ cclinks
val countriesGraph = Graph(cnodes, clinks)

/** import the GraphStream library */
import org.graphstream.graph.implementations._

// create a new instance of GraphStream's SingleGraph class
// using the countriesGraph
val graph: SingleGraph = new SingleGraph("countriesGraph")

// set up the visual attributes for graph visualization
graph.addAttribute("ui.stylesheet","url(file:.//style/stylesheet)")
graph.addAttribute("ui.quality")
graph.addAttribute("ui.antialias")

// load the graphX vertices into GraphStream nodes
for ((id:VertexId, place:PlaceNode) <- countriesGraph.vertices.collect())
{
  val node = graph.addNode(id.toString).asInstanceOf[SingleNode]
  node.addAttribute("name", place.name)
  node.addAttribute("ui.label", place.name)

  if (place.isInstanceOf[Metro])
    node.addAttribute("ui.class", "metro")
  else if(place.isInstanceOf[Country])
    node.addAttribute("ui.class", "country")
  else if(place.isInstanceOf[Continent])
    node.addAttribute("ui.class", "continent")
}

// load the graphX edges into GraphStream edges
for (Edge(x,y,_) <- countriesGraph.edges.collect()) {
  graph.addEdge(x.toString ++ y.toString, x.toString, y.toString,true).
    asInstanceOf[AbstractEdge]
}

// display the graph
graph.display()
