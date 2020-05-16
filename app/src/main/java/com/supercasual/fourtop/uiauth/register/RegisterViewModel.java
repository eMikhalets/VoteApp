package com.supercasual.fourtop.uiauth.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {

    private RegisterRepository registerRepository;
    private MutableLiveData<String> apiRegister;
    private MutableLiveData<String> apiLoginCheck;
    private MutableLiveData<String> apiEmailCheck;
    private MutableLiveData<String> email;
    private MutableLiveData<String> login;
    private MutableLiveData<String> password;
    private MutableLiveData<String> confPass;
    private MutableLiveData<String> name;
    private boolean emailIsFree;
    private boolean loginIsFree;
    private boolean passIsMatched;
    private boolean emailIsValid;
    private boolean loginIsValid;
    private boolean passIsValid;
    private boolean confPassIsValid;
    private boolean nameIsValid;

    public RegisterViewModel() {
        registerRepository = new RegisterRepository();
        apiRegister = new MutableLiveData<>();
        apiLoginCheck = new MutableLiveData<>();
        apiEmailCheck = new MutableLiveData<>();
        email = new MutableLiveData<>();
        login = new MutableLiveData<>();
        password = new MutableLiveData<>();
        confPass = new MutableLiveData<>();
        name = new MutableLiveData<>();
        emailIsFree = false;
        loginIsFree = false;
        passIsMatched = false;
        emailIsValid = false;
        loginIsValid = false;
        passIsValid = false;
        confPassIsValid = false;
        nameIsValid = false;
    }

    public LiveData<String> getApiRegister() {
        return apiRegister;
    }

    public LiveData<String> getApiLoginCheck() {
        return apiLoginCheck;
    }

    public LiveData<String> getApiEmailCheck() {
        return apiEmailCheck;
    }

    public MutableLiveData<String> getEmail() {
        return email;
    }

    public MutableLiveData<String> getLogin() {
        return login;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getConfPass() {
        return confPass;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setEmailIsFree(boolean emailIsFree) {
        this.emailIsFree = emailIsFree;
    }

    public void setLoginIsFree(boolean loginIsFree) {
        this.loginIsFree = loginIsFree;
    }

    public void setPassIsMatched(boolean passIsMatched) {
        this.passIsMatched = passIsMatched;
    }

    public void setEmailIsValid(boolean emailIsValid) {
        this.emailIsValid = emailIsValid;
    }

    public void setLoginIsValid(boolean loginIsValid) {
        this.loginIsValid = loginIsValid;
    }

    public void setPassIsValid(boolean passIsValid) {
        this.passIsValid = passIsValid;
    }

    public void setConfPassIsValid(boolean confPassIsValid) {
        this.confPassIsValid = confPassIsValid;
    }

    public void setNameIsValid(boolean nameIsValid) {
        this.nameIsValid = nameIsValid;
    }

    public void checkPassMatch() {
        String passStr = password.getValue();
        String confPassStr = confPass.getValue();

        if (passStr != null && !passStr.isEmpty() && confPassStr != null && !confPassStr.isEmpty()
                && passStr.equals(confPassStr)) {
            passIsMatched = true;
        } else {
            passIsMatched = false;
        }
    }

    public void register(String email, String login, String password, String name) {
        if (loginIsFree && emailIsFree && passIsMatched) {
            if (emailIsValid && loginIsValid && passIsValid && confPassIsValid && nameIsValid) {
                registerRepository.registerRequest(email, login, password, name,
                        result -> apiRegister.setValue(result));
            }
        }
    }

    public void checkEmail(String email) {
        registerRepository.checkEmailRequest(email, result -> {
            if (result.equals("OK")) {
                emailIsFree = true;
            } else {
                emailIsFree = false;
            }
        });
    }

    public void checkLogin(String login) {
        registerRepository.checkLoginRequest(login, result -> {
            if (result.equals("OK")) {
                loginIsFree = true;
            } else {
                loginIsFree = false;
            }
        });
    }
}
