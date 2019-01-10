import psycopg2

URL = "placeholder"
USERNAME = "placeholder"
PASSWORD = "placeholder"

# function to change unicode to str
def uni_to_str(unicode_input):
    if unicode_input is not None:
        return unicode_input.encode('ascii','ignore')
    
# insert data into database
def scrape(creator, name, desc, date, media_type, url):
    # check if data is formatted correctly
    if creator is None or name is None or desc is None or date is None or url is None:
        return 1
    
    name = name.replace("'", "")

    # connect to database
    connection = psycopg2.connect(host=URL,database=USERNAME, user=USERNAME, password=PASSWORD)
    
    cursor = connection.cursor()
    eventsColumn = "(id, creator, name, description, date, type, url, isglobal)";
    sql = "INSERT INTO events " + eventsColumn +" VALUES (NEXTVAL('event_id'), '" + creator + "','" + name + "', '" + desc + "', '" + date +"', '" + media_type + "', '"+ url + "', 'TRUE')"
    
    #check if title already exists
    check_exist = "SELECT * FROM events WHERE name = '" + name + "'"
    
    cursor.execute(check_exist)
    result = cursor.fetchone()
    if result is None:
        try:
            cursor.execute(sql)
            connection.commit()
        except:
            connection.rollback()
            return 1
    
    cursor.close()
    connection.close()    
    return 0