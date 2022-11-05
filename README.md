## How To Download a Server ?
Just add that in your code:
```java
MinecraftServerDownloader.downloadServer(ServerType.PAPER, "1.19.1", "path/to/file/Paper-1.19.1.jar");
```

### ServerType
- SPIGOT
- PAPER
- CRAFTBUKKIT

## How To Download a BungeeCord or a Waterfall ?
For BungeeCord:
```java
MinecraftServerDownloader.downloadBungeeCord("path/to/file/BungeeCord.jar");
```
For Waterfall:
```java
MinecraftServerDownloader.downloadWaterfall("1.19", "path/to/file/Waterfall.jar");
```
