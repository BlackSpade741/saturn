import requests
import json
import psycopg2
import scraper
import urllib2

def concertScrape(limitOfNew):
    key = 'Dv4TzTBMtO5GJ57Dcrf0Jbxst8fEQHLx'
    secret = 'lgAN5MVcjmUM30rB'
    url = 'https://app.ticketmaster.com/discovery/v2/events.json?size=' + str(limitOfNew) + '&classificationName=concert&apikey=Dv4TzTBMtO5GJ57Dcrf0Jbxst8fEQHLx'
    
    contents = urllib2.urlopen(url).read()
    data = json.loads(contents)
    
    for i in range(len(data["_embedded"]["events"])):
        try:
            event = data["_embedded"]["events"][i]
            
            # Get all necessary info from json
            creator = event["promoter"]["name"]
            date = event["dates"]["start"]["localDate"]
            name = event["name"]
            url = event["images"][0]["url"]
            desc = event["url"]
            
            # Change from unicode str
            creator = scraper.uni_to_str(creator)
            date = scraper.uni_to_str(date)
            name = scraper.uni_to_str(name)
            url= scraper.uni_to_str(url)
            desc = scraper.uni_to_str(desc)
            
            print(creator, date, name, url, desc)
                    
            scraper.scrape(creator, name, desc, date, "concert", url)
        except:
            continue
    
if __name__ == "__main__":
    concertScrape(7)