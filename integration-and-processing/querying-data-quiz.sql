/* Querying data in PostgreSQL - Quiz */

-- the highest level that the team has reached in gameclicks
SELECT MAX(teamlevel)
FROM gameclicks;

-- amount of user id that have reached the highest teamlevel
SELECT COUNT(userid)
FROM gameclicks
WHERE teamlevel = 8;

-- amount of user id that reached the highest level in game-clicks
-- and also clicked the highest costing price in buy-clicks
SELECT COUNT(game.userid)
FROM gameclicks AS game
INNER JOIN buyclicks AS buy ON game.userid = buy.userid
WHERE game.teamlevel = 8 AND buy.price = 20;
