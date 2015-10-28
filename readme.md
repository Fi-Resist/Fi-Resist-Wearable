# Server Install instructions

1. Install [Node.js](https://nodejs.org/en/)
2. Navigate to `<repo>/server`
3. Run `npm install`

# Running

`node index.js`

# Test

`npm test`


# Phone (firefighter)

1. Install [Maven](https://maven.apache.org/)
2. Install [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
3. Install [Android SDK](http://developer.android.com/sdk/index.html)
4. Navigate to `<repo>/phone`
5. Add a [string resource](http://developer.android.com/guide/topics/resources/string-resource.html) called *host* which corresponds to the IP Address of the server
5. Run `mvn package`
6. Install the APK located in `<repo>/phone/target` to the emulator/Android platform of choice



