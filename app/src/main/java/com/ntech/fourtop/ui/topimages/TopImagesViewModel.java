package com.ntech.fourtop.ui.topimages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;
import com.ntech.fourtop.network.pojo.DataImage;
import com.ntech.fourtop.network.pojo.ResponseImages;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TopImagesViewModel extends ViewModel {

    private AppRepository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<String> throwable;
    private MutableLiveData<String> errorMessage;
    private MutableLiveData<List<DataImage>> images;
    private String userToken = "";

    public TopImagesViewModel() {
        repository = AppRepository.get();
        disposables = new CompositeDisposable();
        throwable = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        images = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<String> getThrowable() {
        return throwable;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<DataImage>> getImages() {
        return images;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public void topPhotosRequest() {
        Timber.d("Send top photos request");
        Disposable disposable = repository.topPhotosRequest(userToken, "10", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    private void onSuccess(ResponseImages response) {
        int status = response.getStatus();
        Timber.d("Top photos request status %d", status);

        switch (status) {
            case 200:
                images.setValue(response.getData());
                break;
            case 500:
                errorMessage.setValue(response.getErrorMsg());
                break;
        }
    }

    private void onError(Throwable t) {
        Timber.d(t);
        throwable.setValue(t.toString());
    }
}
