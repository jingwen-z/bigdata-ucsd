// Week 5 - Computing Platforms for Graph Analytics
//
// Hands On: Building A Graph

/** import the GraphX libraries */

// set log level to error, suppress info and warn messages
import org.apache.log4j.Logger
import org.apache.log4j.Level

Logger.getLogger("org").setLevel(Level.ERROR)
Logger.getLogger("akka").setLevel(Level.ERROR)

// Import the Spark's GraphX and RDD libraries along with Scala's source library
import org.apache.spark.graphx._
import org.apache.spark.rdd._

import scala.io.Source

/** import the vertices */
Source.fromFile("./EOADATA/metro.csv").getLines().take(5).foreach(println)
Source.fromFile("./EOADATA/country.csv").getLines().take(5).foreach(println)
Source.fromFile("./EOADATA/metro_country.csv").getLines().take(5).foreach(println)

// create case classes for the places (metros and countries)
class PlaceNode(val name: String) extends Serializable
case class Metro(override val name: String, population: Int) extends PlaceNode(name)
case class Country(override val name: String) extends PlaceNode(name)

// Read the comma delimited text file metros.csv into an RDD of Metro vertices
// ignore lines that start with # 
// and map the columns to: id, Metro(name, population)
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

/** import the edges */
val mclinks: RDD[Edge[Int]] =
  sc.textFile("./EOADATA/metro_country.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      Edge(0L + row(0).toInt, 100L + row(1).toInt, 1)
    }

/** create a graph */

// concatenate the two sets of nodes into a single RDD
val nodes = metros ++ countries
val metrosGraph = Graph(nodes, mclinks)

metrosGraph.vertices.take(5)
metrosGraph.edges.take(5)

/** use Spark’s filter method to return vertices in the graph */

// filter all of the edges in metrosGraph that have a source vertex Id of 1
// and create a map of destination vertex Ids
metrosGraph.edges.filter(_.srcId == 1).map(_.dstId).collect()
metrosGraph.edges.filter(_.dstId == 103).map(_.srcId).collect()
