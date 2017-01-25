// counting the number of nodes
match (n: MyNode)
return count(n)

// counting the number of edges
match (n: MyNode)-[r]->()
return count(r)

// finding leaf nodes:
match (n: MyNode)-[r: TO]->(m)
where not ((m)-->())
return m

// finding root nodes:
match (m)-[r: TO]->(n: MyNode)
where not (()-->(m))
return m

// finding triangles:
match (a)-[:TO]->(b)-[:TO]->(c)-[:TO]->(a)
return distinct a, b, c

// finding 2nd neighbors of D:
match (a)-[:TO*..2]-(b)
where a.Name = 'D'
return distinct a, b

// finding the types of a node:
match (n)
where n.Name = 'Afghanistan'
return labels(n)

// finding the label of an edge:
match (n {Name: 'Afghanistan'})<-[r]-()
return distinct type(r)

// finding all properties of a node:
match (n: Actor)
return * limit 20

// finding loops:
match (n)-[r]->(n)
return n, r limit 10

// finding multigraphs:
match (n)-[r1]->(m), (n)-[r2]-(m)
where r1 <> r2
return n, r1, r2, m limit 10

// finding the induced subgraph given a set of nodes:
match (n)-[r: TO]-(m)
where n.Name in ['A', 'B', 'C', 'D', 'E']
AND m.Name in ['A', 'B', 'C', 'D', 'E']
return n, r, m
