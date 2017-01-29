// loading process
LOAD CSV FROM "file:///chat-data/chat_create_team_chat.csv" AS row
MERGE (u: User {id: toInt(row[0])}) MERGE (t: Team {id: toInt(row[1])})
MERGE (c: TeamChatSession {id: toInt(row[2])})
MERGE (u)-[:CreatesSession{timeStamp: row[3]}]->(c)
MERGE (c)-[:OwnedBy{timeStamp: row[3]}]->(t)
;
LOAD CSV FROM "file:///chat-data/chat_join_team_chat.csv" AS row
MERGE (u:User {id: toInt(row[0])})
MERGE (c:TeamChatSession {id: toInt(row[1])})
MERGE (u)-[:Joins{timeStamp: row[2]}]->(c)
;
LOAD CSV FROM "file:///chat-data/chat_leave_team_chat.csv" AS row
MERGE (u: User {id: toInt(row[0])})
MERGE (c: TeamChatSession {id: toInt(row[1])})
MERGE (u)-[:Leaves{timeStamp: row[2]}]->(c)
;
LOAD CSV FROM "file:///chat-data/chat_item_team_chat.csv" AS row
MERGE (u: User {id: toInt(row[0])})
MERGE (c: TeamChatSession {id: toInt(row[1])})
MERGE (i: ChatItem {id: toInt(row[2])})
MERGE (u)-[:CreateChat{timeStamp: row[3]}]->(i)
MERGE (i)-[:PartOf{timeStamp: row[3]}]->(c)
;
LOAD CSV FROM "file:///chat-data/chat_mention_team_chat.csv" AS row
MERGE (i: ChatItem {id: toInt(row[0])})
MERGE (u: User {id: toInt(row[1])})
MERGE (i)-[:Mentioned{timeStamp: row[2]}]->(u)
;
LOAD CSV FROM "file:///chat-data/chat_respond_team_chat.csv" AS row
MERGE (ione:ChatItem {id: toInt(row[0])})
MERGE (itwo:ChatItem {id: toInt(row[1])})
MERGE (ione)-[:ResponseTo{timeStamp: row[2]}]->(itwo)

// Question 1
// Find the longest conversation chain in the chat data using the "ResponseTo"
// edge label. This question has two parts

// 1) How many chats are involved in it?
match p = (i1)-[:ResponseTo*]->(i2)
return length(p)
order by length(p) desc limit 1

// 2) How many users participated in this chain?
match p = (i1)-[:ResponseTo*]->(i2)
where length(p) = 9
with p
match (u)-[:CreateChat]->(i)
where i in nodes(p)
return count(distinct u)

// Question 2
// Do the top 10 the chattiest users belong to the top 10 chattiest teams?

// chattiest users
match (u)-[:CreateChat*]->(i)
return u.id, count(i)
order by count(i) desc limit 10

// chattiest teams
match (i)-[:PartOf*]->(c)-[:OwnedBy*]->(t)
return t.id, count(c)
order by count(c) desc limit 10

// final answer
match (u)-[:CreateChat*]->(i)-[:PartOf*]->(c)-[:OwnedBy*]->(t)
return u.id, t.id, count(c)
order by count(c) desc limit 10
