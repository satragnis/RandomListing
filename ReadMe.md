
## App Architecture


This sample app used MVVM with Clean architecture



**The modular domains are as follows :-**

1. **Domain** -  
2. **DataRepository** - contains repository via db or network call 
3. **UI** - has simple 2 sections a. for searching images b. for sharing and commenting 


**Data flow:**

1. The UI asks the viewmodel to get/post required data
2. ViewModel calls respective use-case for feature
3. The Use-case calls repository 
4. aRepository fetch data from network/local database and pass back to usecase
5. Usecase pass back the result from repository to viewmodel
6. ViewModel do specific action on data and pass data to UI using LiveData/ Data-binding

