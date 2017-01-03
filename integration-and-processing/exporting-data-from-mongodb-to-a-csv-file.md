# Export data from MongoDB to a CSV file

## Synopsis

`mongoexport` is a utility that produces a JSON or CSV export of data stored in 
a MongoDB instance.

## Options

Argument | Description
:------- | :------- 
`--db <name>` | The database to use.
`--collection <name>` | The collection to use.
`--type=<type>` | Format of export, either csv or json.
`--fields <field 1>,<field 2>,<...>` | The fields to include in the query result.
`--out <name>` | The name of the output file.

## Coding in terminal shell

```shell
# start MongoDB server
cd Downloads/big-data-3/mongodb
./mongodb/bin/mongod --dbpath db

# open a new terminal shell window, export tweet data from MongoDB to a CSV file
cd Downloads/big-data-3/mongodb
./mongodb/bin/mongoexport --db sample --collection users --type-csv --fields tweet_text --out /big-data/tweet-text.csv
```

## Reference

[mongoexport][mongoexport] document on [mongoDB Documentation][mongoDB Documentation]
 website

[mongoexport]: https://docs.mongodb.com/manual/reference/program/mongoexport/
[mongoDB Documentation]: https://docs.mongodb.com/
