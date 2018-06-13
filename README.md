errorbar
========
[![Download](https://api.bintray.com/packages/hendraanggrian/maven/errorbar/images/download.svg) ](https://bintray.com/hendraanggrian/maven/errorbar/_latestVersion)
[![Build Status](https://travis-ci.org/hendraanggrian/errorbar.svg)](https://travis-ci.org/hendraanggrian/errorbar)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

![demo][demo]

Errorbar is an extended version of Snackbar with logo and maximum height.
It is useful to display an error or empty state that requires full attention in a sense that the app workflow should not continue without them being resolved.

Download
--------
This library relies heavily on private resources and internal classes from [Android's support design library][design].

```gradle
repositories {
    google()
    jcenter()
}

dependencies {
    compile 'com.hendraanggrian:errorbar:0.4'
}
```

Usage
-----
Errorbar is everything a Snackbar is, with some modifications:
 * Errorbar stretch its height to match its parent size, unlike Snackbar's wrapping height.
 * Errorbar has default current app theme's background color, unlike Snackbar's dark background.
 * In addition to Snackbar's properties, Errorbar has backdrop as background replacement and logo.
 
#### Programatically
Create Errorbar just like a Snackbar.

```kotlin
Errorbar.make(parent, "No internet connection", Errorbar.LENGTH_INDEFINITE)
    .setImageDrawable(R.drawable.errorbar_ic_cloud)
    .setAction("Retry", { v -> 
        // do something
    })
    .show()
```

Or with Kotlin DSL.

```kotlin
parent.errorbar {
    logoDrawable = R.drawable.errorbar_ic_cloud
    setAction("Retry", { v -> 
        // do something
    })
}
```

#### Styling
Customize Errorbar default text appearance, logo and backdrop with styling.

```xml
<resources>
    <style name="MyAppTheme" parent="Theme.Appcompat.Light.NoActionBar">
        <item name="errorbarStyle">@style/MyErrorbarStyle</item>
    </style>
    
    <style name="MyErrorbarStyle" parent="Widget.Design.Errorbar">
        <item name="logo">@drawable/ic_error</item>
        <item name="android:textSize">24sp</item>
        <item name="actionTextColor">@color/blue</item>
    </style>
</resources>
```

See [attrs.xml][attrs] for complete list of attributes.

#### Limitation
Since Errorbar borrows Snackbar's codebase, Android will treat it as another Snackbar.
It would mean that a View cannot have more than one Snackbar or Errorbar at the same time.
When a Snackbar appear, an attached Errorbar will disappear, and vice-versa.

License
-------
    Copyright 2017 Hendra Anggrian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 
[demo]: /art/demo.gif
[attrs]: /errorbar/res/values/attrs.xml
[design]: https://github.com/android/platform_frameworks_support/tree/master/design