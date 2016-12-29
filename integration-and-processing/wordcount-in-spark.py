"""
Week 4 - Big Data processing tools and systems

Hands-on - WordCount in Spark
"""

lines = sc.textFile('hdfs:/user/cloudera/words.txt')
lines.count()

# split each line into words
# flatMap() method iterates over every line in the RDD
# lambda notation is an anonymous function in Python
# lambda line : line.split('') is executed on each line
words = lines.flatMap(lambda line: line.split(''))
words.take(5)


# assign initial count value to each word
# map() method iterates over every word in the words RDD
# lambda expression creates a tuple with the word and a value of 1
tuples = words.map(lambda word: (word, 1))
tuples.take(5)

# sum all word count values
# reduceByKey() method calls the lambda expression for all the tuples with
# the same word
counts = tuples.reduceByKey(lambda a, b: (a + b))
counts.take(5)

# write word counts to text file in HDFS
# coalesce() method combines all the RDD partitions into a single partition
# since we want a single output file
# saveAsTextFile() writes the RDD to the specified location
counts.coalesce(1).saveAsTextFile('hdfs:/user/cloudera/wordcount/outputDir')
