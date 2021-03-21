
## App Architecture


This sample app used MVVM with Clean architecture



**The modular domains are as follows :-**

1. **Domain** -  contains dataRespository declarations and use-cases
2. **DataRepository** - contains repository via db or network call 
3. **UI** - has simply 2 sections a. for searching images b. for sharing and commenting 


**Data flow:**

1. The UI communicates with the viewmodel component to GET/POST data or store it in the DB
2. The viewModel makes calls to the respective use-case for feature
3. The Use-case in turn makes calls the dataRepository 
4. The dataRepository fetches data from network/local DB and pass back to use-case
5. Use-case passes back the result from repository to the viewmodel
6. The ViewModel does actions on the data and passes the data onto UI via LiveData/ Data-binding

