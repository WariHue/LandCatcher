package com.github.warihue.landcatcher.plugin

import io.github.monun.tap.fake.FakeEntityServer

class SchedulerTask(
    private val fakeEntityServer: FakeEntityServer
) : Runnable {

    override fun run() {
        fakeEntityServer.update()
    }
}