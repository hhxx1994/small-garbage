import thread

import core
import model
import settings


def get_communitylist(city):
    res = []
    for community in model.Community.select().where(model.Community.link.contains(city)):
        res.append(community.title)
    return res


def main_method():
    regionlist = settings.REGIONLIST  # only pinyin support
    model.database_init()
    for city in settings.CTTYS:
        settings.CITY = city
        core.initBASE_URL()
        # thread.start_new_thread(core.GetHouseByRegionlist, (regionlist,))
        # thread.start_new_thread(core.GetRentByRegionlist, (regionlist,))
        # thread.start_new_thread(core.GetCommunityByRegionlist, (regionlist,))
        # core.GetHouseByRegionlist(regionlist)
        # core.GetRentByRegionlist(regionlist)
        # core.GetCommunityByRegionlist(regionlist)  # Init,scrapy celllist and insert database; could run only 1st time
        communitylist = get_communitylist(city)  # Read celllist from database
        thread.start_new_thread(core.GetSellByCommunitylist, (communitylist,))
        # core.GetSellByCommunitylist(communitylist)
    while True:
        pass


if __name__ == "__main__":
    # core.insert_img()
    core.get_img()
