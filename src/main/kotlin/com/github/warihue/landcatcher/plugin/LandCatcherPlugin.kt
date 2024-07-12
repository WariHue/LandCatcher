package com.github.warihue.landcatcher.plugin

import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.core.ChunkMapRenderer
import com.github.warihue.landcatcher.core.damageGun
import com.github.warihue.landcatcher.core.healGun
import com.github.warihue.landcatcher.core.masterLandCatcher
import com.github.warihue.landcatcher.core.util.DataManager
import com.github.warihue.landcatcher.GunsEventListener
import com.github.warihue.landcatcher.weapon.Bullet
import com.github.warihue.landcatcher.weapon.NeedleShot
import io.github.monun.kommand.kommand
import org.bukkit.plugin.java.JavaPlugin
import io.github.monun.tap.fake.FakeEntityServer
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.MapMeta
import org.bukkit.map.MapView
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class LandCatcherPlugin: JavaPlugin() {
    var players: HashMap<Player ,Team> = HashMap()
    var teams: EnumMap<Team, MutableList<Player>> = EnumMap(Team::class.java)
    var chunks: EnumMap<Team, MutableList<Pair<Int, Int>>> = EnumMap(Team::class.java)
    companion object {
        lateinit var instance: LandCatcherPlugin

        lateinit var fakeServer: FakeEntityServer

        lateinit var overWorld: World

        var mapID: Int = 12345
    }

    override fun onEnable() {
        instance = this

        fakeServer = FakeEntityServer.create(this)

        overWorld = server.getWorld("world")!!

        server.scheduler.runTaskTimer(this, fakeServer::update, 0L, 1L)

        server.scheduler.runTaskTimer(this, Bullet.manager::update, 0L, 1L)

        server.scheduler.runTaskTimer(this, NeedleShot.manager::update, 0L, 1L)

        server.pluginManager.registerEvents(EventListener(), this)

        server.pluginManager.registerEvents(GunsEventListener(), this)

        init()

        if(!dataFolder.exists()) dataFolder.mkdirs()
        logger.info(File("dataFolder", "chunks.json").exists().toString())
        if(File(dataFolder, "chunks.json").exists()) {
            logger.info("aa")
            File(dataFolder, "chunks.json").bufferedReader().use { br ->
                val content = br.readText()
                chunks = DataManager.convertJsonToChunkData(content)
                logger.info(content)
                logger.info(DataManager.convertJsonToChunkData(content).toString())
            }
        }

        kommand {
            register("map"){
                requires { isPlayer }
                executes {
                    if(player.inventory.itemInOffHand.type != Material.AIR){player.sendMessage(text("왼손을 비우고 사용해 주세요")); return@executes}
                    val mapItem: ItemStack = ItemStack(Material.FILLED_MAP, 1)
                    val mapMeta: MapMeta = mapItem.itemMeta as MapMeta
                    val mapView = Bukkit.getMap(mapID)
                    if(mapView != null){
                        val mapRenderer = ChunkMapRenderer()
                        mapView.addRenderer(mapRenderer)
                        mapView.centerX = 0
                        mapView.centerZ = 0
                        mapMeta.displayName(text("점령 표시기").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false))
                        mapMeta.mapView = mapView
                        mapItem.itemMeta = mapMeta
                        player.inventory.setItemInOffHand(mapItem)
                    }else{
                        player.sendMessage(text("지도 데이터를 찾을수 없습니다. 개발자에게 문의하세요").color(NamedTextColor.RED))
                    }

                }
            }
            register("catcher"){
                requires { isPlayer }
                executes {
                    player.inventory.addItem(masterLandCatcher(16))
                }
            }
            register("guns"){
                requires { isPlayer }
                executes {
                    player.inventory.addItem(damageGun())
                    player.inventory.addItem(healGun())
                }
            }
        }
    }

    override fun onDisable() {
        logger.info(dataFolder.toString())
        if (!File(dataFolder, "chunks.json").exists()) File(dataFolder, "chunks.json").createNewFile()
        if(File(dataFolder, "chunks.json").exists())
            File(dataFolder, "chunks.json").writeText(DataManager.convertChunkDataToJson(chunks))
        server.scheduler.cancelTasks(this)
    }
    private fun init(){
        teams[Team.NONE] = mutableListOf()
        teams[Team.BLUE] = mutableListOf()
        teams[Team.RED] = mutableListOf()
        teams[Team.GREEN] = mutableListOf()
        teams[Team.YELLOW] = mutableListOf()
        chunks[Team.NONE] = mutableListOf()
        chunks[Team.BLUE] = mutableListOf()
        chunks[Team.RED] = mutableListOf()
        chunks[Team.GREEN] = mutableListOf()
        chunks[Team.YELLOW] = mutableListOf()
        val mapView: MapView = Bukkit.createMap(overWorld)

        mapID = mapView.id
    }
}