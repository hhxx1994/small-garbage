INSERT INTO maplocation(NAME)

SELECT DISTINCT(a.community) FROM
(
SELECT DISTINCT(community) FROM sellinfo
UNION
SELECT DISTINCT(region) FROM rentinfo
UNION
SELECT DISTINCT(community) FROM houseinfo
UNION
SELECT DISTINCT(district) FROM community ) a WHERE community NOT IN (SELECT NAME FROM maplocation)