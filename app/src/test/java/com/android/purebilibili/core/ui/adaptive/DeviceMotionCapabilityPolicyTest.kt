package com.android.purebilibili.core.ui.adaptive

import kotlin.test.Test
import kotlin.test.assertEquals

class DeviceMotionCapabilityPolicyTest {

    @Test
    fun lowRamDevice_forcesReduced() {
        assertEquals(
            MotionTier.Reduced,
            resolveDeviceMotionCapabilityTier(
                isLowRamDevice = true,
                memoryClassMb = 512,
                cpuCores = 8,
            ),
        )
    }

    @Test
    fun veryLowMemoryClass_forcesReduced() {
        assertEquals(
            MotionTier.Reduced,
            resolveDeviceMotionCapabilityTier(
                isLowRamDevice = false,
                memoryClassMb = 192,
                cpuCores = 8,
            ),
        )
    }

    @Test
    fun weakQuadCoreWithLowMemory_forcesReduced() {
        assertEquals(
            MotionTier.Reduced,
            resolveDeviceMotionCapabilityTier(
                isLowRamDevice = false,
                memoryClassMb = 256,
                cpuCores = 4,
            ),
        )
    }

    @Test
    fun typicalMidrangePhone_usesNormal() {
        assertEquals(
            MotionTier.Normal,
            resolveDeviceMotionCapabilityTier(
                isLowRamDevice = false,
                memoryClassMb = 256,
                cpuCores = 8,
            ),
        )
    }

    @Test
    fun highMemoryMultiCore_usesEnhanced() {
        assertEquals(
            MotionTier.Enhanced,
            resolveDeviceMotionCapabilityTier(
                isLowRamDevice = false,
                memoryClassMb = 512,
                cpuCores = 8,
            ),
        )
    }

    @Test
    fun reduceMotionAlwaysWinsOverDeviceCapability() {
        assertEquals(
            MotionTier.Reduced,
            resolveVideoCardTransitionMotionTier(
                reduceMotion = true,
                deviceCapabilityTier = MotionTier.Enhanced,
            ),
        )
        assertEquals(
            MotionTier.Enhanced,
            resolveVideoCardTransitionMotionTier(
                reduceMotion = false,
                deviceCapabilityTier = MotionTier.Enhanced,
            ),
        )
    }
}
