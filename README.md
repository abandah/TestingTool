# FeedBackHandler

[![Release](https://jitpack.io/v/abandah/testingTool.svg?style=flat-square)](https://jitpack.io/#abandah/testingTool)

> FeedBack handling library for Android and Java

Encapsulate FeedBack handling logic into objects that adhere to configurable defaults. Then pass them around as parameters or inject them via Web Service. 

Building with JitPack
=====

If you are using Gradle to get a GitHub project into your build, you will need to:

**Step 1.** Add the JitPack maven repository to the list of repositories:

```
gradle
   allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }

    }
}
```

**Step 2.**  Add the dependency information:

```
dependencies {
    implementation 'com.github.abandah:testingTool:0.0.5'
}
```

**Step 3.**  Create new Class extends com.feedback.handler.App

```
public class app extends com.feedback.handler.App {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getFeedbackLink() {
        return "FeedbackLink";
    }

    @Override
    protected String ErrorLink() {
        return "ErrorLink";
    }

    @Override
    protected String getUserId() {
        return "UserId";
    }
}
```
**Step 4.**  Add  android:name=".app" under application in manifist:

```
 <application
        android:name=".app"
        .
        .
        
```
# Server Side

using Form collection retrieve the values of form elements posted to the HTTP request body, with a form using the POST method.
the values are :
1- localClassName (the class name )
2- localFragmentClassName ( the fragment name if exist )
3- notetext ( note provided by user )
4- error_Product (Application Name)
5- error_Customer (Android)
6- userId (user Id provided in App)
and filnally the screenshot picture 
