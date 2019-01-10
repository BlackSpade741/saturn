import json
import urllib3
import psycopg2

url = ["http://www.omdbapi.com/?i=tt", '&apikey=a5fcc20']
sqlQuery = """INSERT INTO events (id, name, type, date, url, isglobal, description, creator) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"""

# Connect to SQL database
conn = psycopg2.connect(host="tantor.db.elephantsql.com",database="tjlevpcn", user="tjlevpcn", password="SlQEEkbB5hwPHBQxbyrEziDv7w5ozmUu")
cur = conn.cursor()

MonthToDec = {"Jan": "01", "Feb": "02", "Mar": "03", "Apr": "04", "May": "05", "Jun": "06", "Jul": "07", "Aug": "08", "Sep": "09", "Oct": "10", "Nov": "11", "Dec": "12"}

http = urllib3.PoolManager()


def scrapeIMDB(limitOfNew = 1):
    """
    Adds <limitOfNew> new movies to the database

    :param limitOfNew: Number of new games to add to database
    :return: None
    """
    i = 0
    while limitOfNew is not 0:
        # Get app-specific details
        i += 1

        id = str(i).rjust(7, '0')
        response = http.request("GET", url[0] + id + url[1])
        JSONData = json.loads(response.data)

        if JSONData['Response'] is 'False':
            # Movie doesn't exist anymore
            print("Skip\n")
            continue

        # Get release date
        date = JSONData['Released'].split(" ")

        # Format release date
        if date[0] == 'N/A':
            date = '0-0-0'
        else:
            date = date[2] + "-" + MonthToDec[date[1]] + "-" + date[0]

        # Get description
        description = JSONData['Plot']

        try:
            # Try to add game to database
            cur.executemany(sqlQuery, [(str(i), JSONData['Title'], "movie", date, JSONData['Poster'], "true", description, JSONData['Director'])])
            conn.commit()
            print(JSONData['Title'], "added!\n")
            limitOfNew -= 1
        except Exception as e:
            # If game already exists in database
            #print(e)
            print(JSONData['Title'], "already added!\n")
            conn.rollback()
            continue


if __name__ == "__main__":
    scrapeIMDB(100)
