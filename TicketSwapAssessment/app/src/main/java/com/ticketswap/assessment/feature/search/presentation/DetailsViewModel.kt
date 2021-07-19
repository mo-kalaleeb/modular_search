package com.ticketswap.assessment.feature.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ticketswap.assessment.feature.search.domain.datamodel.SearchItemType
import com.ticketswap.assessment.feature.search.domain.datamodel.SpotifyDataModel
import com.ticketswap.assessment.feature.search.domain.usecase.LoadArtistDetailsUseCase
import com.ticketswap.extention.Failure
import com.ticketswap.network.UnauthorizedException
import com.ticketswap.platform.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val loadArtistDetailsUseCase: LoadArtistDetailsUseCase
) : BaseViewModel() {

    private val _detailsLiveDate = MutableLiveData<SpotifyDataModel>()
    val details: LiveData<SpotifyDataModel> = _detailsLiveDate

    fun loadDetails(id: String, type: SearchItemType) {
        Log.d(TAG, "loadDetails: $id")
        loadArtistDetailsUseCase.execute(observer = DetailsObserver(), params = id)
    }

    private inner class DetailsObserver : DisposableSingleObserver<SpotifyDataModel>() {
        override fun onSuccess(t: SpotifyDataModel) {
            setLoading(false)
            _detailsLiveDate.postValue(t)
        }

        override fun onError(e: Throwable?) {
            Log.d(TAG, "onError: $e")
            setLoading(false)
            if (e is UnauthorizedException) {
                handleFailure(Failure.UnauthorizedError)
            }
            handleFailure(Failure.NetworkConnection)
        }
    }

    companion object {
        private const val TAG = "DetailsViewModel"
    }
}
