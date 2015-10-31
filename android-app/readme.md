# FiResist Firefighter Application

This is the application to deploy onto the phones of active firefighters.

## Configuration

In `src/main/res/values` create a file called `host.xml` to define the IP address of the server you're connecting to. For example:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
  <string name="host">http://192.168.1.1</string>
</resources>
```

