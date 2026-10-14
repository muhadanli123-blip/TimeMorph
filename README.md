# TimeMorph (Fabric 1.21.1)
Oyuncu ve mobların altındaki yuvarlak golge Minecraft saatine gore degisir
(varsayilan: gunduz normal, gece daha buyuk ve soluk; gecisler yumusak).

## Jar almanin en kolay yolu (GitHub)
1. github.com'da bos bir repo ac, bu klasorun icerigini yukle (.github klasoru dahil).
2. Actions sekmesi -> "build" -> bitince "timemorph-jar" artifact'ini indir (zip icinde .jar).
3. Jar'i Fabric API ile birlikte mods klasorune at.

## Yerelde: JDK 21 + Gradle 8.10+  ->  gradle build  ->  build/libs/timemorph-1.0.0.jar

## Ayar: config/timemorph.json
day/night ShadowRadius ve ShadowOpacity, affectPlayers, affectMobs, gecis saatleri.
