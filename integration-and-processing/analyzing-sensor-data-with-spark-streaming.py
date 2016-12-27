"""
Week 5 - Big Data analytics using Spark

Hands-on - Analyzing Sensor Data with Spark DataFrames
"""

import re
def parse(line):
	match = re.search("Dm=(\d+)", line)
	if match:
		val = match.group(1)
		return [int(val)]
	return []

from pyspark.streaming import StreamingContext
ssc = StreamingContext(sc, 1)

# create DStream of weather data
lines = ssc.socketTextStream("rtd.hpwren.ucsd.edu", 12028)

# read measurement
# using flatMap() to iterate over the lines DStream
# calling the parse() function we defined above to get the average wind speed
vals = lines.flatMap(parse)

# create sliding window of data
# create a new DStream called window
# that combines the ten seconds worth of data and moves by five seconds
window = vals.window(10, 5)

# define and call analysis function
# first prints the entire contents of the RDD by calling the collect() method
# this is done to demonstrate the sliding window and would not be practical
# if the RDD was containing a large amount of data
# then, check if the size of the RDD is greater than zero before printing
# the maximum and minimum values
def stats(rdd):
	print(rdd.collect())
	if rdd.count() > 0:
		print("max = {}, min = {}",format(rdd.max(), rdd.min()))

# call the stats() function defined above for each RDD in the DStream window
window.foreachRDD(lambda rdd: stats(rdd))

# start the stream processing
ssc.start()
# The sliding window contains ten seconds worth of data
# and slides every five seconds
ssc.stop()
