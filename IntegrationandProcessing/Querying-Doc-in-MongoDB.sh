# -----------------------------
# Querying documents in MongoDB
# -----------------------------

########## Hands on ##########

db.users.count()

# find distinct values
db.users.findOne()

# search for specific field value
db.users.find({user_name: "ActionSportsJax"}).pretty()

# filter fields returned by query
db.users.find({user_name: "ActionSportsJax"}, {tweet_ID: 1, _id: 0})

# perform regular expression search
db.users.find({tweet_text: /FIFA/}).count()

# search using text index
db.users.createIndex({"tweet_text": "text"})
db.users.find({$text: {$search: "FIFA"}}).count()

# search using operators
db.users.find({tweet_mentioned_count: {$gt: 6}})
db.users.find({
    $where: "this.tweet_mentioned_count > this.tweet_followers_count"
}).count()

db.users.find({
    $and: [
        {tweet_text: /FIFA/},
        {tweet_mentioned_count: {$gt: 4}}
    ]
}).count()

########## Quiz ##########

# the username of the twitter account who has a tweet_followers_count of 8973882
db.users.find({tweet_followers_count: 8973882}).pretty()
