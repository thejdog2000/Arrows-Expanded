# Minecraft Arrows Expanded

Fabric mod for Minecraft `26.1.2` that lets players cycle special bow arrow modes with left click.

## Controls

- Hold a bow and left click.
- The first left click only shows the currently selected arrow mode.
- Each later left click advances one step through the carousel:
  `Regular -> Web 3x1 -> Lightning -> Explosive -> Napalm Explosive -> Knock Back -> Teleport -> Regular`
- The selected mode is shown in the action bar and written to the log.
- Shoot the bow normally to fire the selected arrow type.

## Arrow Modes

- `Regular`: vanilla arrow behavior.
- `Web 3x1`: places a 3-block vertical cobweb column on impact.
- `Lightning`: summons lightning on impact.
- `Explosive`: creates a small block-breaking explosion.
- `Napalm Explosive`: creates a small non-block-breaking explosion, fire around impact, and briefly burns hit entities.
- `Knock Back`: launches the hit entity away from the impact.
- `Teleport`: teleports the shooter to the arrow impact.

## Commands

```mcfunction
/arrowsexpanded status
/arrowsexpanded reload
/mae status
/mae reload
```

## Build

```sh
./gradlew build
```

The playable jar is generated at:

```text
build/libs/minecraft-arrows-expanded-0.1.0.jar
```
