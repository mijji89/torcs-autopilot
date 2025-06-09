import pandas as pd

# Carica il file CSV con separatore ';'
df = pd.read_csv("datasetBet.csv", delimiter=';')
df1=pd.read_csv("datasetMic.csv", delimiter=';')
df2=pd.read_csv("datasetReb.csv", delimiter=';')
df3=pd.read_csv("datasetAndre.csv", delimiter=';')
# Converte tutte le colonne numeriche da stringa a float (se necessario)
df = df.apply(pd.to_numeric, errors='coerce')
df1 = df1.apply(pd.to_numeric, errors='coerce')
df2 = df2.apply(pd.to_numeric, errors='coerce')
df3 = df3.apply(pd.to_numeric, errors='coerce')
# Calcola min e max per ogni variabile
min_valuesBet = df.min()
max_valuesBet = df.max()
min_valuesMic = df1.min()
max_valuesMic = df1.max()
min_valuesReb = df2.min()
max_valuesReb = df2.max()
min_valuesAndre = df3.min()
max_valuesAndre = df3.max()
# Unisce i risultati in un unico DataFrame
print('Minimi:')
all_mins = pd.DataFrame({
    'MinimoBet': min_valuesBet,
    'MinimoMic': min_valuesMic, 
    'MinimoReb': min_valuesReb,
    'MinimoAndre': min_valuesAndre
})

print(all_mins)

print('Massimi:')
all_maxs = pd.DataFrame({
    'MinimoBet': max_valuesBet,
    'MassimoMic': max_valuesMic,
    'MassimoReb': max_valuesReb, 
    'MassimoAndre': max_valuesAndre
})

print(all_maxs)


final_min=all_mins.min(axis=1)
final_max=all_maxs.max(axis=1)
print('Massimo');
print(final_max);
print('Minimo');
print(final_max);  