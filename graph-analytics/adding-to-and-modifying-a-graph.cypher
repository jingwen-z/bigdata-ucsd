// adding a node correctly
match (n: ToyNode {name: 'Julian'})
merge (n)-[:ToyRelation {relationship: 'fiancee'}]
->(m: ToyNode {name: 'Joyce', job: 'store clerk'})

// adding a node incorrectly
create (n: ToyNode {name: 'Julian'})-[:ToyRelation {relationship: 'fiancee'}]
->(m: ToyNode {name: 'Joyce', job: 'store clerk'})

// correct your mistake by deleting the bad nodes and edge
match (n: ToyNode {name: 'Joyce'})-[r]-(m)
delete n, r, m

// modify a node’s information
match (n: ToyNode)
where n.name = 'Harry'
set n.job = 'drummer'

match (n: ToyNode)
where n.name = 'Harry'
set n.job = n.job + ['lead guitarist']
