package com.android.purebilibili.feature.settings

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class IOSSlidingSegmentedControlPolicyTest {

    @Test
    fun forceLiquidIndicatorWithoutExternalBackdropKeepsRequestDelegated() {
        val request = resolveIosSlidingSegmentedLiquidGlassRequest(
            forceLiquidIndicator = true,
            hasExternalBackdrop = false
        )

        assertNull(request)
    }

    @Test
    fun forceLiquidIndicatorWithExternalBackdropRequestsLiquidGlass() {
        val request = resolveIosSlidingSegmentedLiquidGlassRequest(
            forceLiquidIndicator = true,
            hasExternalBackdrop = true
        )

        assertEquals(true, request)
    }

    @Test
    fun inactiveForceLiquidIndicatorKeepsRequestDelegated() {
        val request = resolveIosSlidingSegmentedLiquidGlassRequest(
            forceLiquidIndicator = false,
            hasExternalBackdrop = true
        )

        assertNull(request)
    }
}
