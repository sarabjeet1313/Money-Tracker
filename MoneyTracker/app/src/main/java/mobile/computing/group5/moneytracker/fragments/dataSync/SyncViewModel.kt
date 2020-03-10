package mobile.computing.group5.moneytracker.fragments.dataSync

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SyncViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is sync Fragment"
    }
    val text: LiveData<String> = _text
}