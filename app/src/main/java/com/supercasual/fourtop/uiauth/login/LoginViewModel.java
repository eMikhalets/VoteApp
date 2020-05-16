package com.supercasual.fourtop.uiauth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {

    private LoginRepository loginRepository;
    private MutableLiveData<String> apiLogin;
    private MutableLiveData<String> login;
    private MutableLiveData<String> password;
    private boolean loginIsValid;
    private boolean passwordIsValid;

    public LoginViewModel() {
        loginRepository = new LoginRepository();
        apiLogin = new MutableLiveData<>();
        login = new MutableLiveData<>();
        password = new MutableLiveData<>();
        loginIsValid = false;
        passwordIsValid = false;
    }

    public LiveData<String> getApiLogin() {
        return apiLogin;
    }

    public MutableLiveData<String> getLogin() {
        return login;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void setLoginIsValid(boolean loginIsValid) {
        this.loginIsValid = loginIsValid;
    }

    public void setPasswordIsValid(boolean passwordIsValid) {
        this.passwordIsValid = passwordIsValid;
    }

    public void loginRequest(String login, String password) {
        if (loginIsValid && passwordIsValid) {
            loginRepository.loginRequest(login, password, result -> apiLogin.setValue(result));
        } else {
            apiLogin.setValue("ERROR");
        }
    }
}
