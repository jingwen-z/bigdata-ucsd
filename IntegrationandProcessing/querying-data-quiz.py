"""
Week 2 - Querying Data

Quiz - Exploring Pandas DataFrames
"""

import pandas

buyclicksDF = pandas.read_csv('buy-clicks.csv')
buyclicksDF.head(5)
buyclicksDF.shape

# filter rows and columns of a dataframe
buyclicksDF[['price', 'userId']].head(5)
buyclicksDF[buyclicksDF['price'] < 3].head(5)

# calculate sum and average of a column
buyclicksDF['price'].sum()
buyclicksDF['price'].mean()

# combine two dataframes
adclicksDF = pandas.read_csv('ad-clicks.csv')
adclicksDF.head(5)

mergeDF = adclicksDF.merge(buyclicksDF, on = 'userId')
mergeDF.head(5)
