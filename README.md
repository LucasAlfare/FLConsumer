# FLConsumer

This is my own experiment in creating an application that consumes an external API. In this example, was chosen the _GitHub API_ itself, where we take data from it and populate a basic User Interface with that information.

Also, at this point, this application example, works by providing basic information about GitHub users. This is achieved by entenring the GitHub login name of the user and, if it exists, the basic information is shown below the input form.

# Implementation helpers

This example was made in order to use:

- [`Ktor`](https://ktor.io/) dependencies as the main client provider;
- [`gson`](https://github.com/google/gson) to parse the JSON responses;
- [`Coil`](https://coil-kt.github.io/coil/compose/) library, to help load images asynchronously inside the composables tree;
- My own custom event listening library [`FLListening`](https://github.com/LucasAlfare/FLListening), to help decouple UI from backend/model management.

All of those implementations versions can be detailed reviewed in the [build.gradle.kts](https://github.com/LucasAlfare/FLConsumer/blob/master/android/build.gradle.kts#L9) file.

# Data retrieved

With the main JSON response in hands, the data extract from it is:
- Avatar URL;
- Login;
- Name;
- Public Repos;
- Followers.

This example is in progress and should be improved by collecting more data about, e.g., specific public user repositories, making then be listed in UI, as well.

# Android Emulator Screenshots

- Seraching for `LucasAlfare` user:
<img src="img/ss1.png" width="600" height="300">

- Seraching for `JetBrains` user:
<img src="img/ss2.png" width="600" height="300">

# [License](https://github.com/LucasAlfare/FLConsumer/blob/master/LICENSE)
