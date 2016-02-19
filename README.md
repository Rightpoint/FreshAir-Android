# Fresh Air - Android

Fresh Air is an application update library. It is designed to check a remote location for application updates and if updates are available, it prompts the user to upgrade the application. When the new version of the application is started, a dialog can be presented that showcases the new features to the user.

# Basic Setup

Fresh Air can be set up with just a few simple steps. A working example can also be found in the `sample` module of this project.

## Including In Your Project

Fresh Air is available on our Bintray Maven repository and can be included in your project with the following changes.

1. Include our repository in your root project `build.gradle`:

  ```groovy
  allprojects {
    repositories {
        jcenter()
        maven { url "https://dl.bintray.com/raizlabs/Libraries" }
    }
  }
  ```

2. Include the dependency in your module's `build.gradle`:

  ```groovy
  dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:23.1.0'
    
    ...
    
    compile 'com.raizlabs:FreshAir:0.1.0'
  }
  ```
  
3. Initialize the library before use, typically in your `Application` class:

  ```java
  public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FreshAir.initialize(this);
        ...
    }
  }
  ```


## Update Checks
Fresh Air can check for new versions of your application using a remotely hosted JSON file. Just call the following method from the `onCreate()` of your `Application`, your first `Activity`, or any other place you would like to check for updates. Note that this process is asynchronous.

   ```java
  public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FreshAir.initialize(this);
        FreshAir.checkForUpdates("https://raw.githubusercontent.com/Raizlabs/FreshAir-Android/develop/Schema/release_notes.json");
        ...
    }
  }
  ```
  
  *See `Schema/release_notes.json` in this repo for an example of the JSON schema*

## Release Notes
Fresh Air can present a dialog of release notes to showcase the features that you have added in new versions of your application. This dialog will include a scrollable view of features which each contain an image, title, and description. These will be shown once per version, but not again.

1. Define the release information that you wish to display. This doesn't display the information, but lets Fresh Air know what you will want to display when the time comes. Typically, you want to put this information in your `Application.onCreate()` after initializing the library. The release notes follow a `Builder` pattern which takes a set of `Features`, which are also constructed through a `Builder` pattern:

  ```java
  public class SampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FreshAir.initialize(this);
        FreshAir.setReleaseNotes(new ReleaseNotes.Builder()
                .addFeature(
                        new FeatureInfo.Builder()
                                .setImageResource(R.mipmap.ic_launcher)
                                .setTitleResource(R.string.Feature1_Title)
                                .setDescriptionResource(R.string.Feature1_Description)
                )
                .addFeature(
                        new FeatureInfo.Builder()
                                .setImageResource(R.drawable.feature2)
                                .setTitleResource(R.string.Feature2_Title)
                                .setDescriptionResource(R.string.Feature2_Description)
                )
                .addFeature(
                        new FeatureInfo.Builder()
                                .setImageResource(R.drawable.feature3)
                                .setTitleResource(R.string.Feature3_Title)
                                .setDescriptionResource(R.string.Feature3_Description)
                )
                // Sometimes handy for minor updates: see docs
                .setVersionCode(1));
    }
  }
  ```
  
2. When you would like to display the release notes dialog, call the following method. This requires a `FragmentActivity` from the support library. Typically you want to do this from your main launcher `Activity` or any other entry point to your application:

  ```java
  public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        ...
        
        FreshAir.showReleaseNotes(this);
    }
  }
  ```
  
## Extras
Additional flexibility is available. Consult the JavaDoc for more details, but some of the higher level functionality is listed below.

- **Update Prompts** - You can change the text contents of the update prompt via `FreshAir.setUpdatePrompt(UpdatePromptInfo)`. `UpdatePromptInfo` contains a `Builder` class for easy overriding.
- **Clear History** - You can clear the previously shown updates, forced updates, and release notes via `FreshAir.clearFUpdatePromptVersion()`, `FreshAir.clearForcedUpdateVersion()`, and `FreshAir.clearReleaseNotesVersion`
- **App Disabling** - You can manually block out the app via `FreshAir.disableApp()`. To unset this, you must call `FreshAir.clearAppDisabled()` and restart the app.
- **Logging** - You can alter the level of logging via `FreshAir.setLogLevel(int)` to clear logging or add more logging for debugging. By default, only warnings and errors are displayed.
