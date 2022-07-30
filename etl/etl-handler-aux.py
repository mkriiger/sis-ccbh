import csv

#  0    1       2           3          4       5       6         7        8     9         10          11            12             13              14               15                 16       17
# "","Tags","SeqName","Description","Length","Hits","e.Value","sim_mean","GO","GO_Ids","GO_Names","Enzyme_Codes","Enzyme_Names","InterPro_Ids","InterPro_GO_Ids","InterPro_GO_Names","id_ccbh","g_o"


'''with open('ccbh4851-copy.csv', newline='') as f_input, open('ccbh-blast2go.csv', 'w', newline='') as f_output:
    csv_input = csv.reader(f_input)
    csv_output = csv.writer(f_output)   
    
    for row in csv_input:
        blast2go_go_ids = row[9].split(';')
        blast2go_go_names = row[10].split(';')       
        for i in range(len(blast2go_go_ids)):            
            csv_output.writerow([row[16], row[2], row[3], row[4], row[5], row[6],  
                blast2go_go_ids[i], blast2go_go_names[i]])

with open('ccbh4851-copy.csv', newline='') as f_input, open('ccbh-interpro.csv', 'w', newline='') as f_output:
    csv_input = csv.reader(f_input)
    csv_output = csv.writer(f_output)   
    
    for row in csv_input:        
        interpro_go_ids = row[14].split(';')
        interpro_go_names = row[15].split(';')
        for i in range(len(interpro_go_ids)):
            csv_output.writerow([row[16], row[2], row[3], row[4], row[5], row[6],  
                interpro_go_ids[i], interpro_go_names[i]])'''

with open('ccbh4851-copy.csv', newline='') as f_input, open('ccbh-enzyme.csv', 'w', newline='') as f_output:
    csv_input = csv.reader(f_input)
    csv_output = csv.writer(f_output)   
    
    for row in csv_input:
        if(row[11] != ''):        
            enzyme_codes = row[11].split(';')
            enzyme_names = row[12].split(';')
            for i in range(len(enzyme_codes)):
                csv_output.writerow([row[16],   
                    enzyme_codes[i], enzyme_names[i]])








