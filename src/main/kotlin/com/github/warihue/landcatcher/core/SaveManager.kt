package com.github.warihue.landcatcher.core

import com.github.warihue.landcatcher.Team
import io.github.monun.tap.config.Config
import io.github.monun.tap.config.ConfigSupport
import io.github.monun.tap.config.Name
import java.io.File

@Name("player")
class PlayerCfg(team: Team){
    @Config
    val team = team
}

fun deleteFile(file: File){
    file.delete()
}

fun makeFile(file: File, team: Team): PlayerCfg{
    val data = PlayerCfg(team)
    ConfigSupport.compute(data, file, true)
    return data
}
