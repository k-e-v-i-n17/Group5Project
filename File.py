import json
def getName(list):
    nameList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[0]
        nameList.append(y)
    return nameList

def getDate(list):
    DateList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[2]
        DateList.append(y)
    return DateList

list = []
with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/project/target/classes/com/metrics/project/Datafile.txt') as f:
    for line in f:
        list.append(line)
dateList = getDate(list)
nameList = getName(list)

dictionary = {
    dateList[0] : nameList[0]
}
json_object = json.dumps(dictionary, indent=2)
with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/project/target/classes/com/metrics/project/dateName.json', 'w') as f:
    f.write(json_object)

for i in range(1, len(dateList)):
    with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/project/target/classes/com/metrics/project/dateName.json') as file:
        dictObj = json.load(file)
    dictObj.update({dateList[i] : nameList[i]})
    with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/project/target/classes/com/metrics/project/dateName.json', 'w') as json_file:
        json.dump(dictObj, json_file, indent=4,  separators=(',',': '))


