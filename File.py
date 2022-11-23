import json
def getName(list):                               #set all the names into a list
    nameList = []                                #make a new list
    for i in range(1, len(list)):                #loop through the list file
        str = list[i]                            #get the line from the list
        read = str.split(',')                    #separate out by commas
        y = read[0]                              #set the address you want to get the information from
        nameList.append(y)                       #add to the list you create before
    return nameList                              #return the list to the main

def getAdd(list):                                #set all the additions into a list
    addList = []
    for i in range(1, len(list)):                                                  
        str = list[i]
        read = str.split(',')
        y = read[5]
        addList.append(y)
    return addList

def getDel(list):                                #set all the deletions into a list
    delList = []
    for i in range(1, len(list)):
        str = list[i]
        read = str.split(',')
        y = read[6]
        delList.append(y)
    return delList

list = []                                        #list to contain date from Datafile.txt
with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/Datafile.txt') as f:          #set the address of the Datafile
    for line in f:                               #loop through the text file and add to the list
        list.append(line)
nameList = getName(list)
addList = getAdd(list)
delList = getDel(list)

NameList = [nameList[0]]                         #set the start point
AddList = [addList[0]]
DelList = [delList[0]]
c = 0                                            #set a count to use inside the loop

for i in range(1, len(nameList)):                #to add up all the information for the same person
    name = nameList[i]
    if name != NameList[c]:                      #if is a new name
        c += 1                                   #count+1 cause we now have a new person
        NameList.append(name)                    #add the name to the list
        temp = addList[i]                        #get value store in addition list
        AddList.append(str(temp))                #store it into the final list for addition
        temp = delList[i]                        #do the same thing with deletion list
        DelList.append(str(temp))
    else:                                        #if is a name we have already
        temp = addList[i]
        temp = int(temp) + int(AddList[c])       #add the addition to the addition we have before for this person
        AddList[c] = str(temp)                   #replace into the addition list
        temp = delList[i]                        #do the same for deletion list
        temp = int(temp) + int(DelList[c])
        DelList[c] = str(temp)


dictionary = {                                                              #set the dictionary to start with
    NameList[0] : [AddList[0]] + [DelList[0]]                               #eg: "Ann":["10","11"]
}
json_object = json.dumps(dictionary, indent=2)                              #create the object by dictionary
with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/AddDelData.json', 'w') as f:    #write the dictionary into the json file
    f.write(json_object)

for i in range(1, len(NameList)):                                           #loop through all the other information and add to the json file
    with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/AddDelData.json') as file:
        dictObj = json.load(file)                                           #load the old information in
    dictObj.update({NameList[i] : [AddList[i]] + [DelList[i]]})             #update with new information
    with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/AddDelData.json', 'w') as json_file:
        json.dump(dictObj, json_file, indent=4,  separators=(',',': '))     #store the new information


