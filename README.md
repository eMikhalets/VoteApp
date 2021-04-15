# VoteApp ![Logo](/app/src/main/res/mipmap-xhdpi/ic_launcher.png)

## The application allows you to upload photos to the server and vote between two random images ##

To use the application, you need to create an account, the email may be fake and is only needed to be able to reset the password.

The backend is implemented on Firebase. The account system is based on Firebase Authentication. Photos are stored on Cloud Firestore. Also, the Firebase Realtime Database stores information about images and users, such as rating, name, links to the cloud.

Used architecture pattern - Model-View-ViewModel

## Implemented features ##

1. View images uploaded to the server;
2. Upload and crop your own images;
3. Voting among two random images;
4. Change username, profile photo and password;
5. View top images and users.

## Install ##

1. Download apk file from [app/release/app-release.apk](app/release/app-release.apk);
2. Allow third-party apps to be installed on your android phone;
3. Install apk.

## Used libraries ##

1. [Firebase Authentication, Realtime Database, Cloud](https://firebase.google.com/);
2. [Dagger](https://github.com/google/dagger);
3. [Android Architecture Components](https://developer.android.com/topic/libraries/architecture);
4. [Mockito](https://site.mockito.org/);
5. [Picasso](https://square.github.io/picasso/);
6. [Timber](https://github.com/JakeWharton/timber);
7. [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer);
8. [PhotoView](https://github.com/Baseflow/PhotoView) allows zooming ImageView;
9. [Android Image Cropper](https://github.com/ArthurHub/Android-Image-Cropper).

## Screenshots ##

<img src="/screenshots/Screenshot_1.jpg" width="250"> <img src="/screenshots/Screenshot_2.jpg" width="250"> <img src="/screenshots/Screenshot_3.jpg" width="250"> <img src="/screenshots/Screenshot_4.jpg" width="250"> <img src="/screenshots/Screenshot_5.jpg" width="250"> <img src="/screenshots/Screenshot_6.jpg" width="250"> <img src="/screenshots/Screenshot_7.jpg" width="250"> <img src="/screenshots/Screenshot_8.jpg" width="250">
