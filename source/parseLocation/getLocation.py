# coding=utf-8
import json



import requests
from peewee import *

database = MySQLDatabase(
    "test",
    host="127.0.0.1",
    port=3306,
    user="root",
    passwd="123456",
    charset='utf8',
    use_unicode=True,
)


class BaseModel(Model):
    class Meta:
        database = database


class MapLocation(BaseModel):
    id = BigIntegerField(primary_key=True)
    data = CharField()
    lng = CharField()
    lat = CharField()
    precise = CharField()
    confidence = CharField()
    level = CharField()
    name = CharField()
    status = IntegerField()


# database.connect()
# database.create_tables([MapLocation], safe=True)
# database.close()


query = MapLocation.select(MapLocation.name, MapLocation.id).where(MapLocation.status == 0)
for data in query:
    column = data.name
    cid = data.id
    BASE_URL = 'http://api.map.baidu.com/geocoder/v2/?address={0}&output=json&ak=GxVf1346nT7Arfj4myBss3vehiGBLANq'
    url = BASE_URL.format(column.encode("utf-8"))
    response = requests.get(url)
    result = response.content.decode("utf-8")
    print(result)
    jsonData = json.loads(result)
    if jsonData['status'] == 0:
        lng = jsonData['result']['location']['lng']

        lat = jsonData['result']['location']['lat']

        precise = jsonData['result']['precise']

        confidence = jsonData['result']['confidence']

        level = jsonData['result']['level']

        MapLocation.update(data=result, lng=lng, lat=lat, precise=precise, confidence=confidence, level=level,
                           status=1).where(MapLocation.id == cid).execute()
