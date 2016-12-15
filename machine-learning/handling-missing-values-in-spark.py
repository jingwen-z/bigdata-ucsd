"""
Week 2 - Data Preparation for Machine Learning

Hands-on: Handling Missing Values in Spark
"""

from pyspark.sql import SQLContext

sqlContext = SQLContext(sc)

df = sqlContext.read.load('file:///home/cloudera/Downloads/big-data-4/daily_weather.csv',
                          format = 'com.databricks.spark.csv',
                          header = 'true',
                          inferSchema = 'true')

df.describe().toPandas().transpose()

df.describe(['air_temp_9am']).show()
df.count()

removeAllDF = df.na.drop()
removeAllDF.describe(['air_temp_9am']).show()
removeAllDF.count()

# impute missing values
from pyspark.sql.functions import avg
imputeDF = df

for x in imputeDF.columns:
	meanValue = removeAllDF.agg(avg(x)).first()[0]
	print(x, meanValue)
	imputeDF = imputeDF.na.fill(meanValue, [x])

# print imputed data summary statistics
df.describe(['air_temp_9am']).show()
imputeDF.describe(['air_temp_9am']).show()
