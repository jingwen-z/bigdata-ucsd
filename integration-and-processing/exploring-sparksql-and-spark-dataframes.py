"""
Week 5 - Big Data analytics using Spark

Hands-on - Exploring SparkSQL and Spark DataFrames
"""

from pyspark.sql import SQLContext

sqlsc = SQLContext(sc)

# format('jdbc') says that the source of the DataFrame will be using a Java 
# database connection
# url option is the URL connection string to access the Postgres database
# dbtable option specifies the gameclicks table

df = sqlsc.read.format('jdbc') \
    .option('url', 'jdbc:postgresql://localhost/cloudera?user=cloudera') \
    .option('dbtable', 'gameclicks') \
    .load()

df.printSchema()
df.count()
df.show(5)

df.select('userid', 'teamlevel').show(5)

df.filter(df['teamlevel'] > 1).select('userid', 'teamlevel').show(5)

df.groupBy('ishit').count().show()

from pyspark.sql.functions import *
df.select(mean('ishit'), sum('ishit')).show()

df2 = sqlsc.read.format('jdbc') \
    .option('url', 'jdbc:postgresql://localhost/cloudera?user=cloudera') \
    .option('dbtable', 'adclicks') \
    .load()

df2.printSchema()

merge = df.join(df2, 'userid')
merge.printSchema()

merge.show(5)
