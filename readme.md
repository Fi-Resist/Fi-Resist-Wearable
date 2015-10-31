# FiResist Application

FiResist contains three components:

1. Android application which connects to wearable and monitors firefighters
2. Socket server which receives real time data from the Android application
3. Ionic (hybrid-mobile app) application which receives real time data from the server about the firefighters

# Installation

### Global Dependencies

- [Maven](https://maven.apache.org/)
- [NodeJS](https://nodejs.org/en/)
- [Ionic](http://ionicframework.com/)

Installation of each application's dependencies is handled through Maven.

### Configuration

**TODO: Write config script**

**For the Android application:**

In `android-app/src/main/res/values` create a file called `host.xml` to define the IP address of the server you're connecting to. For example:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <string name="host">http://192.168.1.1</string>
</resources>
```

**TODO: Make Node components configurable**

