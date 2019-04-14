import pandas as pd
import csv
import os
from bookclub_server.models import Book
with open('datasets/BX-Books.csv') as csvfile:
    reader = csv.DictReader(csvfile)
    for row in reader:
        p = Book(isbn=row['isbn'], title=row['title'], authorName=row['authorName'], publishDate=row['publishDate'], publisher=row['publisher'], bookPhoto=row['bookPhoto'])
        p.save()
exit()