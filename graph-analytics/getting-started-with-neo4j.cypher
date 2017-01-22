// view the resulting graph
match (n:ToyNode)-[r]-(m)
return n, r, m

// delete all nodes and edges
match (n)-[r]-()
delete n, r

// delete all nodes which have no edges
match (n)
delete n

// delete only ToyNode nodes which have no edges
match (n:ToyNode)
delete n

// delete all edges
match (n)-[r]-()
delete r

// delete only ToyRelation edges
match (n)-[r:ToyRelation]-()
delete r

// selecting an existing single ToyNode node
match (n:ToyNode {name:'Julian'})
return n
