# ErrorHandler
[![Download](https://api.bintray.com/packages/workable/maven/ErrorHandler/images/download.svg) ](https://bintray.com/workable/maven/ErrorHandler/_latestVersion)
[![Travis](https://travis-ci.org/Workable/java-error-handler.svg?branch=master)](https://travis-ci.org/Workable/java-error-handler)

> Error handling library for Android and Java

Encapsulate error handling logic into objects that adhere to configurable defaults. Then pass them around as parameters or inject them via DI. 


Building with JitPack
=====

If you are using Gradle to get a GitHub project into your build, you will need to:

**Step 1.** Add the JitPack maven repository to the list of repositories:

```gradle
    url "https://jitpack.io"
```

**Step 2.**  Add the dependency information:

 - *Group:* com.github.Username
 - *Artifact:* Repository Name
 - *Version:* Release tag, commit hash or `master-SNAPSHOT`

**That's it!** The first time you request a project JitPack checks out the code, builds it and sends the Jar files back to you.

To see an example head to [jitpack.io](https://jitpack.io) and 'Look Up' a GitHub repository by url.

Gradle example:
```gradle
    allprojects {
        repositories {
            jcenter()
            maven { url "https://jitpack.io" }
        }
   }
   dependencies {
        implementation 'com.github.abandah:ErrorHandler:0.1.4'
   }
```

*Note*: when using multiple repositories in build.gradle it is recommended to add JitPack *at the end*. Gradle will go through all repositories in order until it finds a dependency.


**Pull Requests**

In addition to snapshot builds JitPack supports building Pull Requests. Simply use `PR<NR>-SNAPSHOT` as the version.

For example:
```gradle
    // dependency for Pull Request 4
    implementation 'com.github.jitpack:gradle-simple:PR4-SNAPSHOT'
```

Publishing on JitPack
======

Publishing your library on JitPack is very simple:

- Create a [GitHub Release](https://github.com/blog/1547-release-your-software)  

As long as there's a build file in your repository and it can install your library in the local Maven repository, it is sufficient for JitPack. See the [Guide to building](BUILDING.md) on how to publish JVM libraries and [Guide to Android](ANDROID.md) on how to publish Android libraries.

*Tip:* You can try out your code before a release by using the commit hash as the version.

**Some extras to consider**

Add dependency information in your README. Tell the world where to get your library:

```gradle
   repositories {
        jcenter()
        maven { url "https://jitpack.io" }
   }
   dependencies {
         implementation 'com.github.jitpack:gradle-simple:1.0'
   }
```  

- Add sources jar. Creating the sources jar makes it easier for others to use your code and contribute.


# Features #

## Javadoc publishing ##

- For a single module project, if it produces a javadoc.jar then you can browse the javadoc files directly at: 
    - `https://jitpack.io/com/github/USER/REPO/VERSION/javadoc/` or
    - `https://jitpack.io/com/github/USER/REPO/latest/javadoc/` (latest release tag)

- For a multi module project, the artifacts are published under `com.github.USER.REPO:MODULE:VERSION`, where `MODULE` is the artifact id of the module (not necessarily the same as the directory it lives in)

- Javadocs for a multi-module project follow the same convention, i.e.

    - `https://jitpack.io/com/github/USER/REPO/MODULE/VERSION/javadoc/` 

- Aggregated javadocs for a multi-module project may be available if the top level aggregates them into a jar and publishes it. The module name in this case is the artifact id of the top level module.

- See the example projects on how to configure your build file ([Android example](https://github.com/jitpack/android-example/blob/master/library/build.gradle)). 

## Other features #

- [Private repositories](https://jitpack.io/private)
- Dynamic versions. You can use Gradle's dynamic version '1.+' and Maven's version ranges for releases. They resolve to releases that have already been built. JitPack periodically checks for new releases and builds them ahead-of-time.
- Build by tag, commit id, or `anyBranch-SNAPSHOT`.
- You can also use your own domain name for group

## Immutable artifacts #

Public repository artifacts on JitPack are immutable after 7 days of publishing. You will see an indicator in the list of versions when a build becomes frozen (snowflake icon).
Withing the first 7 days they can be re-built to fix any release issues. Even then we recommend creating a patch release instead.

JitPack will also keep hosting artifacts after the originating git repository is deleted.
To delete a build you need to have git push permissions to your git repository.

## Other Git hosts ##

JitPack also works with other Git hosting providers. The only difference is the groupId of your artifacts:

 - BitBucket: *org.bitbucket*.Username:Repo:Tag

 - GitLab: *com.gitlab*.Username:Repo:Tag
 
 - Gitee: *com.gitee*.Username:Repo:Tag
 
 - Azure: *com.azure*.Project:Repo:Tag

To see an example, head to https://jitpack.io and 'Look Up' a Git repository by url.

Self-hosted Git servers like GitLab are also supported. You can register your server on your [user page](https://jitpack.io/w/user).

## Custom domain name ##

If you want to use your own domain name as the groupId instead of com.github.yourcompany, you can.
We support mapping your domain name to your GitHub organization. Then, instead of 'com.github.yourcompany' groupId, you can use 'com.yourcompany' while the name of the project and version remains the same.

To enable your own domain name:  

  1. Add a DNS TXT record that maps git.yourcompany.com to https://github.com/yourcompany. This needs to be configured at your domain name provider such as GoDaddy. For example see [How to add a TXT record](https://uk.godaddy.com/help/add-a-txt-record-19232).  

  2. Go to https://jitpack.io/#com.yourcompany/yourrepo and click Look up. If DNS resolution worked then you should see a list of versions.   

  3. Select the version you want and click 'Get it' to see Maven/Gradle instructions.  

Example: [https://jitpack.io/#io.jitpack/gradle-simple](https://jitpack.io/#io.jitpack/gradle-simple)

To check that the DNS TXT record was added, run the command `dig txt git.yourcompany.com`. For example:
```
~$ dig txt git.jitpack.io
...
;; ANSWER SECTION:
git.jitpack.io.		600	IN	TXT	"https://github.com/jitpack"
```

## Badges ##

Add this line to your README.md to show a status badge with the latest release:

```
[![Release](https://jitpack.io/v/User/Repo.svg)]
(https://jitpack.io/#User/Repo)
```


[![Release](https://jitpack.io/v/jitpack/maven-simple.svg)](https://jitpack.io/#jitpack/maven-simple)


If you are using a custom domain or BitBucket, use:

```
[![Release](https://jitpack.io/v/com.example/Repo.svg)]
(https://jitpack.io/#com.example/Repo)


[![Release](https://jitpack.io/v/org.bitbucket.User/Repo.svg)]
(https://jitpack.io/#org.bitbucket.User/Repo)
```

Or, if you prefer the flat-squared style:

```
https://jitpack.io/v/User/Repo.svg?style=flat-square
```

[![Release](https://jitpack.io/v/jitpack/maven-simple.svg?style=flat-square)](https://jitpack.io/#jitpack/maven-simple)

