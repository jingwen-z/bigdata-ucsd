// Week 5 - Computing Platforms for Graph Analytics
//
// Hands On: Building A Graph

// import the GraphX libraries
import org.apache.log4j.Logger
import org.apache.log4j.Level

Logger.getLogger("org").setLevel(Level.ERROR)
Logger.getLogger("akka").setLevel(Level.ERROR)

import org.apache.spark.graphx._
import org.apache.spark.rdd._

import scala.io.Source

// import the vertices
Source.fromFile("./EOADATA/metro.csv").getLines().take(5).foreach(println)
Source.fromFile("./EOADATA/country.csv").getLines().take(5).foreach(println)
Source.fromFile("./EOADATA/metro_country.csv").getLines().take(5).foreach(println)

class PlaceNode(val name: String) extends Serializable
case class Metro(override val name: String, population: Int) extends PlaceNode(name)
case class Country(override val name: String) extends PlaceNode(name)

val metros: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/metro.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (0L + row(0).toInt, Metro(row(1), row(2).toInt))
    }

val countries: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/country.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (100L + row(0).toInt, Country(row(1)))
    }

// import the edges
val mclinks: RDD[Edge[Int]] =
  sc.textFile("./EOADATA/metro_country.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      Edge(0L + row(0).toInt, 100L + row(1).toInt, 1)
    }

// create a graph
val nodes = metros ++ countries
val metrosGraph = Graph(nodes, mclinks)

metrosGraph.vertices.take(5)
metrosGraph.edges.take(5)

// use Spark’s filter method to return vertices in the graph
metrosGraph.edges.filter(_.srcId == 1).map(_.dstId).collect()
metrosGraph.edges.filter(_.dstId == 103).map(_.srcId).collect()
