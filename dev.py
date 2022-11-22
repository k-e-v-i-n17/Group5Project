import pandas as pd
ip_df = pd.read_csv(filepath_or_buffer= "DF.csv", header= 0 )
finalList =[]
for index, row in ip_df.iterrows():
    finalList.append([row['author_name'],row['committer_when']])
print(finalList[0:5])