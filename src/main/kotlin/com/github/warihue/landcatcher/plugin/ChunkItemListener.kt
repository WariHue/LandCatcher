package com.github.warihue.landcatcher.plugin

import com.github.warihue.landcatcher.Team
import com.github.warihue.landcatcher.core.*
import com.github.warihue.landcatcher.core.util.ChunkManager
import com.github.warihue.landcatcher.core.util.ChunkManager.addChunkOnAnother
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.title.Title
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ChunkItemListener: Listener {
    @EventHandler
    fun listenClickItem(event: PlayerInteractEvent) {
        val player = event.player
        val action = event.action
        if (action.isRightClick) {
            //region 마스터 문서
            if (player.inventory.itemInMainHand.correctChecker(masterLandCatcher())) {
                if(ChunkManager.addChunkMaster(
                    LandCatcherPlugin.instance.players[event.player]!!.team,
                    event.player.world.getChunkAt(event.player.location)
                )) {
                    if (player.gameMode != GameMode.CREATIVE)
                        player.inventory.getItem(player.inventory.slotFounder(masterLandCatcher()))!!.amount -= 1

                    for(cP in LandCatcherPlugin.instance.teams[LandCatcherPlugin.instance.players[player]!!.team]!!) {
                        cP.showTitle(Title.title(text("${LandCatcherPlugin.instance.players[player]!!.team}팀이 땅을 얻었습니다!").color(NamedTextColor.WHITE), text("")))
                    }
                }

            }
            //endregion
            //region 빈 땅 문서
            if (player.inventory.itemInMainHand.correctChecker(occupyLandCatcher())) {
                if(ChunkManager.addChunkOnBlank(
                        LandCatcherPlugin.instance.players[event.player]!!.team,
                        event.player.world.getChunkAt(event.player.location)
                    )) {
                    if (player.gameMode != GameMode.CREATIVE)
                        player.inventory.getItem(player.inventory.slotFounder(occupyLandCatcher()))!!.amount -= 1

                    for(cP in LandCatcherPlugin.instance.players.keys) {
                        cP.showTitle(Title.title(text("${LandCatcherPlugin.instance.players[player]!!.team}팀이 땅을 얻었습니다!").color(NamedTextColor.WHITE), text("")))
                    }
                }
                else{
                    player.sendMessage(text("무엇인가 실패한 듯 하다").color(NamedTextColor.RED))
                }
            }
            //endregion
            //region 스틸 땅 문서
            if (player.inventory.itemInMainHand.chunkItemCorrectChecker()) {
                val team = player.inventory.itemInMainHand.itemTeamChecker()
                if(addChunkOnAnother(LandCatcherPlugin.instance.players[player]!!.team, team, player.world.getChunkAt(player.location))){
                    if (player.gameMode != GameMode.CREATIVE)
                        player.inventory.getItem(player.inventory.slotFounder(stealLandCatcher(team)))!!.amount -= 1
                    for(cP in LandCatcherPlugin.instance.players.keys) {
                        cP.showTitle(Title.title(text("${LandCatcherPlugin.instance.players[player]!!.team}팀이 땅을 얻었습니다!").color(NamedTextColor.WHITE), text("")))
                    }
                }
                else{
                    player.sendMessage(text("무엇인가 실패한 듯 하다").color(NamedTextColor.RED))
                }
            }
            else if(player.inventory.itemInMainHand.correctChecker(stealLandCatcher(Team.NONE))){
                if(addChunkOnAnother(LandCatcherPlugin.instance.players[player]!!.team, player.world.getChunkAt(player.location))){
                    if (player.gameMode != GameMode.CREATIVE)
                        player.inventory.getItem(player.inventory.slotFounder(stealLandCatcher(Team.NONE)))!!.amount -= 1
                    for(cP in LandCatcherPlugin.instance.players.keys) {
                        cP.showTitle(Title.title(text("${LandCatcherPlugin.instance.players[player]!!.team}팀이 땅을 얻었습니다!").color(NamedTextColor.WHITE), text("")))
                    }
                }
                else{
                    player.sendMessage(text("무엇인가 실패한 듯 하다").color(NamedTextColor.RED))
                }
            }
            //endregion
        }
    }
}