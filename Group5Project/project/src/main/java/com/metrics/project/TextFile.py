import json
def getName(list):                     #form a list to store names
    nameList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[0]
        nameList.append(y)
    return nameList

def getDate(list):                     #form a list to store date
    dateList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[2]
        dateList.append(y)
    return dateList

def getCom(list):                      #form a list to store commits counts
    comList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[3]
        comList.append(y)
    return comList

def getAdd(list):                      #form a list to store additions
    addList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[5]
        addList.append(y)
    return addList

def getDel(list):                      #form a list to store deletions
    delList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[6]
        delList.append(y)
    return delList

list = []
with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/Datafile.txt') as f:
    for line in f:
        list.append(line)              #add information from the txt file to the list
nameList = getName(list)
dateList = getDate(list)
comList = getCom(list)
addList = getAdd(list)
delList = getDel(list)

text = open("TextData.txt", 'w')       #creat a new text file

writeText = open("TextData.txt", 'a')  #use to append txt into the new txt file
for i in range(0, len(nameList)):      #loop through all the value and add in to the new txt file
    line = nameList[i] + ',' + dateList[i] + ',' + comList[i] + ',' + addList[i] + ',' + delList[i]  #add all the information toghther and separate with commas
    writeText.write(line)              #write the line into the txt file
    writeText.write('\n')              #skip to the next line after input a line each time
writeText.close()                      #close