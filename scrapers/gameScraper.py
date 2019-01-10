import json
import sys
import datetime

url = 'http://store.steampowered.com/api/appdetails?appids=' + sys.argv[1]
import urllib2
contents = urllib2.urlopen(url).read()
data = json.loads(contents)
if data[sys.argv[1]]["success"] == False:
    exit(1)

# example id: 879870
start_node = data[sys.argv[1]]["data"]

# Get all necessary info from json
creator = start_node["developers"][0]
date = str(start_node["release_date"]["date"])
name = start_node["name"]
url = start_node["header_image"]
desc = start_node["short_description"].replace("'", "")

date_format = datetime.datetime.strptime(date, "%d %b, %Y")
date_format = date_format.strftime("%Y-%m-%d")

print(creator, date_format, name, url, desc)
	
scraper.scrape(creator, name, desc, date, "game", url)