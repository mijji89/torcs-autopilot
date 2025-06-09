import pandas as pd


# Carica i file CSV con separatore ';'
df = pd.read_csv("Torc/classes/datasetBet.csv", delimiter=';')
df1=pd.read_csv("Torc/classes/datasetMic.csv", delimiter=';')
df2=pd.read_csv("Torc/classes/datasetReb.csv", delimiter=';')
df3=pd.read_csv("Torc/classes/datasetAndre.csv", delimiter=';')
df4= pd.read_csv("Torc/classes/datasetManovre.csv", delimiter=';')

# Converte tutte le colonne numeriche da stringa a float (se necessario)
df = df.apply(pd.to_numeric, errors='coerce')
df1 = df1.apply(pd.to_numeric, errors='coerce')
df2 = df2.apply(pd.to_numeric, errors='coerce')
df3 = df3.apply(pd.to_numeric, errors='coerce')
df4= df4.apply(pd.to_numeric, errors='coerce')

# Calcola min e max per ogni variabile
min_valuesBet = df.min()
max_valuesBet = df.max()
min_valuesMic = df1.min()
max_valuesMic = df1.max()
min_valuesReb = df2.min()
max_valuesReb = df2.max()
min_valuesAndre = df3.min()
max_valuesAndre = df3.max()
min_valuesManovre=df4.min()
max_valuesManovre=df4.max()

# Unisce i risultati suddividendoli in minimi e massimi 
print('Minimi:')
all_mins = pd.DataFrame({
    'MinimoBet': min_valuesBet,
    'MinimoMic': min_valuesMic, 
    'MinimoReb': min_valuesReb,
    'MinimoAndre': min_valuesAndre,
    'MinimoManovre': min_valuesManovre
})

print(all_mins)

print('Massimi:')
all_maxs = pd.DataFrame({
    'MinimoBet': max_valuesBet,
    'MassimoMic': max_valuesMic,
    'MassimoReb': max_valuesReb, 
    'MassimoAndre': max_valuesAndre,
    'MassimoManovre': max_valuesManovre
})

print(all_maxs)

#Calcolo del minimo tra i minimi e del massimo tra i massimi per ciascuna categoria
final_min=all_mins.min(axis=1)
final_max=all_maxs.max(axis=1)

print('Massimo');
print(final_max);
print('Minimo');
print(final_min);  