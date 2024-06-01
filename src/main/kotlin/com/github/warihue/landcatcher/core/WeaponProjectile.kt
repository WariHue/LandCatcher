package com.github.warihue.landcatcher.core

import io.github.monun.tap.fake.FakeProjectile

open class WeaponProjectile(maxTicks: Int, range: Double) : FakeProjectile(maxTicks, range) {
    lateinit var landCatcher: LandCatcher
        internal set
}