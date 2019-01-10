import psycopg2
import urllib3
import json
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

generalDataURL = "https://api.steampowered.com/ISteamApps/GetAppList/v0002/"
specificDataURL = "https://store.steampowered.com/api/appdetails/?appids="
sqlQuery = """INSERT INTO events (id, name, type, date, url, isglobal, description, creator) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"""

# Grab general data from steam API
http = urllib3.PoolManager()
response = http.request("GET", generalDataURL)
generalData = json.loads(response.data)['applist']['apps']

# Connect to SQL database
conn = psycopg2.connect(host="tantor.db.elephantsql.com",database="tjlevpcn", user="tjlevpcn", password="SlQEEkbB5hwPHBQxbyrEziDv7w5ozmUu")
cur = conn.cursor()

MonthToDec = {"Jan": "01", "Feb": "02", "Mar": "03", "Apr": "04", "May": "05", "Jun": "06", "Jul": "07", "Aug": "08", "Sep": "09", "Oct": "10", "Nov": "11", "Dec": "12"}


def scrapeSteam(limitOfNew = 1):
    """
    Adds <limitOfNew> new games to the database

    :param limitOfNew: Number of new games to add to database
    :return: None
    """
    for game in generalData:
        # Get app-specific details
        print(game['name'])
        response = http.request("GET", specificDataURL + str(game['appid']))

        # Convert JSON to Python
        game = json.loads(response.data)[str(game['appid'])]

        if game['success'] is True:
            # If game exists
            game = game['data']
        else:
            # Game doesn't exist anymore
            print("Skip\n")
            continue

        # Get release date
        date = game['release_date']

        if game['name'] == "Negative Type":
            print(date)

        if date['coming_soon'] is 'true' or date['date'].isnumeric() or len(date['date'].split(" ")) != 3:
            # If no set release date is made
            date = "0-0-0"
        else:
            # Format release date
            date = date['date'].split(" ")
            date = date[2] + "-" + MonthToDec[date[1][:-1]] + "-" + date[0]

        # Get description
        description = game['detailed_description']

        while description.find("<img") is not -1:
            # Remove images from description if any
            description = description[:description.find("<img")] + description[description.find(">", description.find("<img") + 5) + 1:]

        try:
            # Try to add game to database
            cur.executemany(sqlQuery, [(game['steam_appid'], game['name'], "game", date, game['header_image'], "true", description, game['publishers'][0])])
            conn.commit()
            print(game['name'], "added!\n")
            limitOfNew -= 1
        except Exception as e:
            # If game already exists in database
            # print(e)
            print(game['name'], "already added!\n")
            conn.rollback()
            continue

        if limitOfNew <= 0:
            # If hit limit, stop
            break


if __name__ == "__main__":
    scrapeSteam(100)
