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
with open('DataFile.txt', encoding='utf-8') as f:
    for line in f:
        list.append(line)
print(list)
dateList = getDate(list)
nameList = getName(list)
print(list)
dictionary = {
    dateList[0] : nameList[0]
}
json_object = json.dumps(dictionary, indent=2)
with open('dateName.json', 'w') as f:
    f.write(json_object)

for i in range(1, len(dateList)):
    with open('dateName.json') as file:
        dictObj = json.load(file)
    dictObj.update({dateList[i] : nameList[i]})
    with open('dateName.json', 'w') as json_file:
        json.dump(dictObj, json_file, indent=4,  separators=(',',': '))