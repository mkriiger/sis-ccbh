import pandas as pd

'''
# criando o csv correspondente à tabela ccbh
df = pd.read_csv('ccbh4851-copy.csv',usecols=['id_ccbh', 'SeqName', 'Description', 'Length'])
df = pd.DataFrame({'id_ccbh':df['id_ccbh'], 'seq_name':df['SeqName'],  
                    'description':df['Description'], 'length':df['Length']})
df.to_csv('C:/Users/marciokriiger/OneDrive - cefet-rj.br/TCC/ETL/csvs tratados/tb_ccbh.csv', index=False)

# criando o csv correspondente à tabela blast2go
df = pd.read_csv('ccbh-blast2go.csv',usecols=['GO_Ids', 'GO_Names'])
df = pd.DataFrame({'blast2go_go_id':df['GO_Ids'].str.strip(), 'blast2go_go_name':df['GO_Names'].str.strip()})
df.to_csv('C:/Users/marciokriiger/OneDrive - cefet-rj.br/TCC/ETL/csvs tratados/tb_blast2go.csv', index=False)

# criando o csv correspondente à tabela interproscan
df = pd.read_csv('ccbh-interpro.csv',usecols=['InterPro_GO_Ids', 'InterPro_GO_Names'])
df = pd.DataFrame({'interpro_go_id':df['InterPro_GO_Ids'].str.strip(), 'interpro_go_name':df['InterPro_GO_Names'].str.strip()})
df.to_csv('C:/Users/marciokriiger/OneDrive - cefet-rj.br/TCC/ETL/csvs tratados/tb_interpro.csv', index=False)
'''

# criando o csv correspondente à tabela enzyme
df = pd.read_csv('ccbh-enzyme.csv',usecols=['Enzyme_Codes', 'Enzyme_Names'])
df = pd.DataFrame({'enzyme_code':df['Enzyme_Codes'].str.strip(), 'enzyme_name':df['Enzyme_Names'].str.strip()})
df = df.drop_duplicates()
df.to_csv('C:/Users/marciokriiger/OneDrive - cefet-rj.br/TCC/ETL/csvs tratados/tb_enzyme.csv', index=False)

'''
# criando o csv correspondente à tabela de relacionamento ccbh_blast2go
df = pd.read_csv('ccbh-blast2go.csv', dtype=str, usecols=['id_ccbh', 'GO_Ids', 'e.Value', 'Hits'])
df = pd.DataFrame({'id_ccbh':df['id_ccbh'], 'blast2go_go_id':df['GO_Ids'].str.strip(), 'blast2go_e_value':df['e.Value'], 'blast2go_hits':df['Hits']})
df.to_csv('C:/Users/marciokriiger/OneDrive - cefet-rj.br/TCC/ETL/csvs tratados/tb_ccbh_blast2go.csv', index=False)

# criando o csv correspondente à tabela de relacionamento ccbh_interproscan
df = pd.read_csv('ccbh-interpro.csv',usecols=['id_ccbh', 'InterPro_GO_Ids'])
df = pd.DataFrame({'id_ccbh':df['id_ccbh'], 'interpro_go_id':df['InterPro_GO_Ids'].str.strip()})
df.to_csv('C:/Users/marciokriiger/OneDrive - cefet-rj.br/TCC/ETL/csvs tratados/tb_ccbh_interproscan.csv', index=False)
'''
'''# criando o csv correspondente à tabela de relacionamento ccbh_enzyme
df = pd.read_csv('ccbh-enzyme.csv',usecols=['id_ccbh', 'Enzyme_Codes'])
df = pd.DataFrame({'id_ccbh':df['id_ccbh'], 'enzyme_code':df['Enzyme_Codes'].str.strip()})
df.to_csv('C:/Users/marciokriiger/OneDrive - cefet-rj.br/TCC/ETL/csvs tratados/tb_ccbh_enzyme.csv', index=False)'''
