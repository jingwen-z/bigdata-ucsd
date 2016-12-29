"""
Week 2 - Activities for Data Exploration

Quiz: Data Exploration in Spark
"""

from pyspark.sql import SQLContext

sqlContext = SQLContext(sc)

df = sqlContext.read.load('file:///home/cloudera/Downloads/big-data-4/daily_weather.csv',
                          format = 'com.databricks.spark.csv',
                          header = 'true',
                          inferSchema = 'true')

# What is the maximum of the average wind speed measurements at 9am (to 2 
# decimal places)?
df.describe().toPandas().transpose()

# How many rows containing rain accumulation at 9am measurements have missing 
# values?
df.describe('rain_accumulation_9am').show()  # 1089
df.count() - 1089

# What is the correlation between the relative humidity at 9am and at 3pm (to 2 
# decimal places, and without removing or imputing missing values)?
df.stat.corr('relative_humidity_9am', 'relative_humidity_3pm')
