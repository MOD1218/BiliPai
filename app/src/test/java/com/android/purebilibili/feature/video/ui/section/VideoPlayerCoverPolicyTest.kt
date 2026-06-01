package com.android.purebilibili.feature.video.ui.section

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VideoPlayerCoverPolicyTest {

    @Test
    fun verticalVideo_fillsPlayerViewportDuringCoverPhase() {
        assertTrue(
            shouldFillPlayerViewportForManualStartCover(
                shouldKeepCoverForManualStart = false,
                forceCoverDuringReturnAnimation = false,
                isVerticalVideo = true
            )
        )
    }

    @Test
    fun returnCoverSharedBounds_doesNotForceViewportFill() {
        assertFalse(
            shouldFillPlayerViewportForManualStartCover(
                shouldKeepCoverForManualStart = true,
                forceCoverDuringReturnAnimation = true,
                isVerticalVideo = true
            )
        )
    }

    @Test
    fun horizontalManualStartCover_keepsInlineCoverContainer() {
        assertFalse(
            shouldFillPlayerViewportForManualStartCover(
                shouldKeepCoverForManualStart = true,
                forceCoverDuringReturnAnimation = false,
                isVerticalVideo = false
            )
        )
    }

    @Test
    fun verticalManualStartCover_canFillViewport() {
        assertTrue(
            shouldFillPlayerViewportForManualStartCover(
                shouldKeepCoverForManualStart = true,
                forceCoverDuringReturnAnimation = false,
                isVerticalVideo = true
            )
        )
    }

    @Test
    fun forcedReturnCoverSharedBounds_keepsHomeCoverKeyMatchedDuringReturn() {
        // 返回阶段播放器容器会让出 sharedBounds，强制封面必须承接同一个 cover key。
        assertTrue(
            shouldEnableForcedReturnCoverSharedBounds(
                forceCoverDuringReturnAnimation = true,
                transitionEnabled = true,
                hasSharedTransitionScope = true,
                hasAnimatedVisibilityScope = true,
                sourceRoute = com.android.purebilibili.navigation.ScreenRoutes.Home.route
            )
        )
        assertTrue(
            shouldEnableForcedReturnCoverSharedBounds(
                forceCoverDuringReturnAnimation = true,
                transitionEnabled = true,
                hasSharedTransitionScope = true,
                hasAnimatedVisibilityScope = true,
                sourceRoute = "${com.android.purebilibili.navigation.ScreenRoutes.Home.route}?from=tab"
            )
        )
    }

    @Test
    fun forcedReturnCoverSharedBounds_stillActiveForNonHomeCardReturnTargets() {
        listOf("dynamic", "search", "history", "favorite", "watch_later", "partition").forEach { route ->
            assertTrue(
                shouldEnableForcedReturnCoverSharedBounds(
                    forceCoverDuringReturnAnimation = true,
                    transitionEnabled = true,
                    hasSharedTransitionScope = true,
                    hasAnimatedVisibilityScope = true,
                    sourceRoute = route
                ),
                "expected forced cover sharedBounds to remain enabled for sourceRoute=$route"
            )
        }
        assertTrue(shouldUseReturnLandingMotionForForcedReturnCover(true))
        assertFalse(shouldUseReturnLandingMotionForForcedReturnCover(false))
    }

    @Test
    fun coverFirstOverlaySharedBounds_usesSameCardRouteGuardAsReturn() {
        assertTrue(
            shouldEnableCoverOverlaySharedBounds(
                useCoverOverlaySharedBounds = true,
                transitionEnabled = true,
                hasSharedTransitionScope = true,
                hasAnimatedVisibilityScope = true,
                sourceRoute = "partition"
            )
        )
        assertFalse(
            shouldEnableCoverOverlaySharedBounds(
                useCoverOverlaySharedBounds = true,
                transitionEnabled = true,
                hasSharedTransitionScope = true,
                hasAnimatedVisibilityScope = true,
                sourceRoute = "settings"
            )
        )
    }

    @Test
    fun forcedReturnCoverSourceRoute_keepsEveryVideoCardReturnTargetRoute() {
        listOf("home", "dynamic", "search", "history", "favorite", "watch_later", "partition").forEach { route ->
            assertTrue(resolveForcedReturnCoverSharedElementSourceRoute(route) == route)
            assertTrue(resolveForcedReturnCoverSharedElementSourceRoute("$route?from=tab") == route)
        }
        assertTrue(resolveForcedReturnCoverSharedElementSourceRoute("settings") == null)
    }
}
