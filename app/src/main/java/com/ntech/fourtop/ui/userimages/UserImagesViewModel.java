package com.ntech.fourtop.ui.userimages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ntech.fourtop.data.AppRepository;
import com.ntech.fourtop.network.pojo.DataImage;
import com.ntech.fourtop.network.pojo.DataProfile;
import com.ntech.fourtop.network.pojo.ResponseBase;
import com.ntech.fourtop.network.pojo.ResponseImages;

import java.io.File;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class UserImagesViewModel extends ViewModel {

    private AppRepository repository;
    private CompositeDisposable disposables;
    private MutableLiveData<List<DataImage>> images;
    private MutableLiveData<String> throwable;
    private MutableLiveData<DataProfile> profile;
    private MutableLiveData<String> errorMessage;

    public UserImagesViewModel() {
        repository = AppRepository.get();
        disposables = new CompositeDisposable();
        images = new MutableLiveData<>();
        throwable = new MutableLiveData<>();
        profile = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    public LiveData<List<DataImage>> getImages() {
        return images;
    }

    public LiveData<String> getThrowable() {
        return throwable;
    }

    public LiveData<DataProfile> getProfile() {
        return profile;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void galleryRequest() {
        Timber.d("Send gallery request");
        Disposable disposable = repository.galleryRequest("", "10", "0")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    public void galleryAddRequest(String token, File file) {
        Timber.d("Send gallery add request");
        Disposable disposable = repository.galleryAddRequest(token, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSuccess, this::onError);
        disposables.add(disposable);
    }

    public void galleryRemoveRequest(String token, int position) {
        if (images.getValue() != null) {
            String id = String.valueOf(images.getValue().get(position));
            Timber.d("Send gallery remove request");
            Disposable disposable = repository.galleryRemoveRequest(token, id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::onError);
            disposables.add(disposable);
        }
    }

    private void onSuccess(ResponseImages response) {
        int status = response.getStatus();
        Timber.d("Gallery request status %d", status);

        switch (status) {
            case 200:
                images.setValue(response.getData());
                break;
            case 500:
                errorMessage.setValue(response.getErrorMsg());
                break;
        }
    }

    private void onSuccess(ResponseBase response) {
        int status = response.getStatus();
        Timber.d("Gallery change request status %d", status);

        switch (status) {
            case 200:
                galleryRequest();
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
