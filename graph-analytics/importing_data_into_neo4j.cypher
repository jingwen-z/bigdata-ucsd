// one way to "clean the slate" in Neo4j before importing (run both lines)
match (a)-[r]->() delete a,r
match (a) delete a

// script to Import Data Set: test.csv (simple road network)
LOAD CSV WITH HEADERS FROM "file:///C:/coursera/data/test.csv" AS line
MERGE (n:MyNode {Name:line.Source})
MERGE (m:MyNode {Name:line.Target})
MERGE (n) -[:TO {dist:line.distance}]-> (m)
