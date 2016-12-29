"""
Week 3 - Classification Algorithms
Hands-on: Classification in Spark
"""

from pyspark.sql import SQLContext
from pyspark.sql import DataFrameNaFunctions
from pyspark.ml import Pipeline
from pyspark.ml.classification import DecisionTreeClassifier
from pyspark.ml.feature import Binarizer
from pyspark.ml.feature import VectorAssembler, StringIndexer, VectorIndexer

sqlContext = SQLContext(sc)
df = sqlContext.read.load('file:///home/cloudera/Downloads/big-data-4/daily_weather.csv',
                          format = 'com.databricks.spark.csv',
                          header = 'true',
                          inferSchema = 'true')
df.columns

featureColumns = ['air_pressure_9am',
                  'air_temp_9am',
                  'avg_wind_direction_9am',
                  'avg_wind_speed_9am',
                  'max_wind_direction_9am',
                  'max_wind_speed_9am',
                  'rain_accumulation_9am',
                  'rain_duration_9am']

# drop unused data
df = df.drop('number')

# drop missing data
df = df.na.drop()

df.count(), len(df.columns)

# create categorical variable
# threshold argument specifies the threshold value for the variable
# inputCol is the input column to read
# outputCol is the name of the new categorical column
binarizer = Binarizer(threshold = 24.99999,
                      inputCol = 'relative_humidity_3pm',
                      outputCol = 'label')
binarizedDF = binarizer.transform(df)

binarizedDF.select('relative_humidity_3pm', 'label').show(4)

# aggregate features
assembler = VectorAssembler(inputCols = featureColumns, outputCol = 'features')
assembled = assembler.transform(binarizedDF)

# split training and test data
(trainingData, testData) = assembled.randomSplit([0.8, 0.2], seed = 13234)
trainingData.count(), testData.count()

# create and train decision tree
# labelCol argument is the column we are trying to predict
# featuresCol specifies the aggregated features column
# maxDepth is stopping criterion for tree induction based on maximum depth of tree
# minInstancesPerNode is stopping criterion for tree induction based on minimum 
# number of samples in a node
# impurity is the impurity measure used to split nodes
dt = DecisionTreeClassifier(labelCol = 'label',
                            featuresCol = 'features',
                            maxDepth = 5,
                            minInstancesPerNode = 20,
                            impurity = 'gini')

pipeline = Pipeline(stages = [dt])
model = pipeline.fit(trainingData)
predictions = model.transform(testData)
predictions.select('prediction', 'label').show(10)

predictions.select('prediction', 'label').write.save(path = 'file:///home/cloudera/Downloads/big-data-4/predictions.csv',
                                                     format = 'com.databricks.spark.csv',
                                                     header = 'true')
