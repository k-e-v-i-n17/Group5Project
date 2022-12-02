import json
import pandas as pd
ip_df = pd.read_csv(filepath_or_buffer= "DF.csv", header= 0 )
finalList = []
for index, row in ip_df.iterrows():
    finalList.append([row['author_name'],row['committer_when']])

author_count = {}
for row in finalList:
    author = row[0]
    year = row[1][0:4]
    if author in author_count.keys(): 
        cnt = author_count.get(author)+1
        author_count[author]= cnt
    else:
        author_count[author]=0
max_dict ={}
for (k,v) in author_count.items():
    if v>100:
        max_dict[k]=v

print(max_dict)

# Serializing json  
with open('CommitCount.json', 'w') as fp:
    json.dump(max_dict, fp)

# for i in range(1, len(NameList)):                                           #loop through all the other information and add to the json file
# #     with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/AddDelData.json') as file:
# #         dictObj = json.load(file)                                           #load the old information in
# #     dictObj.update({NameList[i] : [AddList[i]] + [DelList[i]]})             #update with new information
# #     with open('C:/Users/134ya/Documents/Code/CSU33012/webdata/Group5Project/AddDelData.json', 'w') as json_file:
# #         json.dump(dictObj, json_file, indent=4,  separators=(',',': '))     #store the new information
