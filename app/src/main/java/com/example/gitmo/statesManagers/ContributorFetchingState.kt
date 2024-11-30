package com.example.gitmo.statesManagers

import com.example.gitmo.domain.models.contributerModel.ContributerDataItem

sealed class ContributorFetchingState {
    class UnTouched() : ContributorFetchingState()
    class Loading() : ContributorFetchingState()
    class Success(val data : List<ContributerDataItem>?) : ContributorFetchingState()
    class Error : ContributorFetchingState()
}