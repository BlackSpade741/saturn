import requests
import json
import psycopg2
import sys
import scraper

def animeScrape(limitOfNew):
    """
    Adds <limitOfNew> new games to the database
     :param limitOfNew: Number of new games to add to database
    :return: None
    """
    query = '''
    query ($id: Int){
    Media (id: $id, type: ANIME) {
        id
        description
        title {
            english
            romaji
            native
        }
        startDate{
            year
            month
            day
        }
        endDate{
            year
            month
            day
        }
        coverImage{
            large
            medium
        }
        isAdult
        studios{
            nodes{
                name
            }
        }
        }
    }
    '''
    id_count = 1
    
    while limitOfNew > 0:
        #15125
        variables = {
            'id': id_count
        }
        
        id_count += 1
        url = 'https://graphql.anilist.co'
        response = requests.post(url, json={'query': query, 'variables': variables})
        data = json.loads(response.text)
        if data.has_key("errors"):
            continue
        
        start_node = data["data"]["Media"]
        
        # Get all necessary info from json
        creator = start_node["studios"]["nodes"][0]["name"]
        date = str(start_node["startDate"]["year"]) + "-" + str(start_node["startDate"]["month"]) +"-"+ str(start_node["startDate"]["day"])
        name = start_node["title"]["english"]
        url = start_node["coverImage"]["large"]
        desc = start_node["description"]
        
        # Change from unicode str
        creator = scraper.uni_to_str(creator)
        name = scraper.uni_to_str(name)
        url= scraper.uni_to_str(url)
        desc = scraper.uni_to_str(desc.replace("'", ""))
        
        print(creator, date, name, url, desc)
        
        error = scraper.scrape(creator, name, desc, date, "anime", url)
        
        if error == 0:
            limitOfNew -= 1
            
    
if __name__ == "__main__":
    animeScrape(20)
