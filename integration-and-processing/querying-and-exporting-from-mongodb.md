# Assignment: Querying and Exporting from MongoDB

Imagine you are the Sports Analyst for a big magazine. The goal of this 
assignment is to demonstrate your data-driven reporting skills and express the 
following natural language questions as MongoDB queries on soccer-related tweets
 in English.

```shell
# start MongoDB server
cd Downloads/big-data-3/mongodb
./mongodb/bin/mongod --dbpath db

# open a new terminal shell window, start MongoDB shell
cd Downloads/big-data-3/mongodb
./mongodb/bin/mongo
```

**Query 1: How many tweets have location not null?**

```shell
db.users.find({"user.Location": {$ne: null}}).count()
# 6937
```

**Query 2: How many people have more followers than friends?**

```shell
db.users.find({
    $where: "this.user.FollowersCount > this.user.FriendsCount"
}).count()
# 5809
```

**Query 3: Return text of tweets which have the string "http://" ?**

```shell
db.users.find({$text: {$search: "http://"}}, {tweet_text: 1, _id: 0})
```

**Query 4: Return all the tweets which contain text "England" but not "UEFA" ?**

```shell
db.users.find({$text: {$search: "England - UEFA"}}, {tweet_text: 1, _id: 0})
```

**Query 5: Get all the tweets from the location "Ireland" and contains the 
string "UEFA"?**

```shell
db.users.find({
    $and: [
        {"user.Location": {$eq: "Ireland"}},
        {tweer_text: /UEFA/}
        ]
    }, {"user.FriendsCount": 1})

# results as following
# {"_id": ObjectId("578ffc547eb951401527b5da"), "user": {"FriendsCount": 2381}}
# {"_id": ObjectId("578ffda17eb951401527b738"), "user": {"FriendsCount": 2277}}
# {"_id": ObjectId("57965ef5c38159118f94f8ae"), "user": {"FriendsCount": 802}}
# {"_id": ObjectId("57966dc0c38159201ca7f637"), "user": {"FriendsCount": 0}}

# then, in order to find the user with the highest "FriendsCount", we can code:
db.users.find({_id: ObjectId("578ffc547eb951401527b5da")}, {user_name: 1})
# the user "ProfitwatchInfo" has the highest "FriendsCount"
