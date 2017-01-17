# Data Exploration 

## Aggregation

Report on your key findings from your aggregation analysis.

```shell
# Amount spent buying items
source = "buy-clicks.csv"
| stats sum(price)
# result: 21407

# Number of unique items available to be purchased
source = "buy-clicks.csv"
| stats count by buyId

# A histogram showing how many times each item is purchased
source = "buy-clicks.csv"
| stats count by buyId

# A histogram showing how much money was made from each item:
source = "buy-clicks.csv"
| stats sum(price) by buyId
```

## Filtering

Report on your key findings from your filtering analysis.

```shell
# A histogram showing total amount of money spent by the top ten users(ranked by
# how much money they spent).
source = "buy-clicks.csv"
| stats sum(price) by userId
| sort 10 num(sum(price)) desc

# The user id, platform, and hit-ratio percentage for the top three buying users
source = "user-session.csv" userId = 2229
| stats count by platformType

source = "user-session.csv" userId = 12
| stats count by platformType

source = "user-session.csv" userId = 471
| stats count by platformType

source = "game-clicks.csv" userId = 2229
| stats sum(isHit) as Sum, count(isHit) as Count by userId
| eval percentage = Sum / Count

source = "game-clicks.csv" userId = 12
| stats sum(isHit) as Sum, count(isHit) as Count by userId
| eval percentage = Sum / Count

source = "game-clicks.csv" userId = 471
| stats sum(isHit) as Sum, count(isHit) as Count by userId
| eval percentage = Sum / Count
```
