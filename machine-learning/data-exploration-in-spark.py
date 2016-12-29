"""
Week 2 - Data Exploration

Hands-on: Data Exploration in Spark
"""

from pyspark.sql import SQLContext

# create an SQLContext
sqlContext = SQLContext(sc)

df = sqlContext.read.load('file:///home/cloudera/Downloads/big-data-4/daily_weather.csv',
                          format = 'com.databricks.spark.csv',
                          header = 'true',
                          inferSchema = 'true')

df.columns
df.printSchema()

df.describe().toPandas().transpose()
df.describe('air_pressure_9am').show()

len(df.columns)
df.count()

df2 = df.na.drop(subset = ['air_pressure_9am'])
df2.count()

df2.stat.corr('rain_accumulation_9am', 'rain_duration_9am')
