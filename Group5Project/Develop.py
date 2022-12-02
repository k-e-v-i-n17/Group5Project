#help(open)
list = []
with read_csv('DF.csv', encoding='utf-8') as f:
    next(f, None)
    for line in f:
        print(line)
        break
    