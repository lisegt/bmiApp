package com.example.mybmiapp.db

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class BmiViewModel(private val repository: BmiRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allBmis: LiveData<List<Bmi>> = repository.allBmis.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(bmi: Bmi) = viewModelScope.launch {
        repository.addBmi(bmi)
    }
}

class WordViewModelFactory(private val repository: BmiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BmiViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BmiViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
