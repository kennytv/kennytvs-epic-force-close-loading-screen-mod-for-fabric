# kennytvs-epic-force-close-loading-screen-mod-for-fabric (also HEAVILY reduces resource pack loading screen duration)

<details>
  <summary>Behavior/info on the world loading screen from before 1.19.3 (mod version 1.1.1 and older)</summary>

Since 1.18.2, the client only quits the world loading screen once the chunk it is in has been received...
*[and at least 2 seconds have passed](https://bugs.mojang.com/browse/MC-249059)*.
By default, the mod fixes that behavior. Additionally, you can also fully get rid of the screen.

## Yeeting the screen *entirely*

If you want to **fully** kill the screen, you can toggle a config option (you
need [modmenu](https://www.curseforge.com/minecraft/mc-mods/modmenu)
and [Cloth Config API](https://www.curseforge.com/minecraft/mc-mods/cloth-config) for that).
**Keep in mind this reintroduces a client bug where you can briefly (only visually) fall through the world if your
network connection is slow or the server is loading chunks very slowly after a world change.**

Here are some video comparisons of Vanilla vs. the mod with the instant-close option enabled:

### Server join (1 vs. 3 seconds)

![Server join](https://i.imgur.com/duhOAYM.gif)

### World change (0 vs. 2 seconds)

![World change](https://imgur.com/GYdJVJE.gif)

</details>

### Resource pack loading

![Resource pack loading](https://imgur.com/Z46vTy3.gif)

Credits to the well-thought-out name and wonderful icon go to [mdcfe](https://github.com/mdcfe).

## Downloads

You can download the mod from [Modrinth](https://modrinth.com/mod/forcecloseworldloadingscreen) or
the [GH downloads page](https://github.com/kennytv/kennytvs-epic-force-close-loading-screen-mod-for-fabric/releases).

## License

This project is licensed under the [MIT license](LICENSE).
