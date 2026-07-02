package com.android.purebilibili.core.ui.transition

import com.android.purebilibili.core.ui.adaptive.MotionTier
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class VideoCardTransitionBackdropPolicyTest {

    @Test
    fun idleSession_returnsInactiveFrame() {
        val frame = resolveVideoCardTransitionBackdropFrame(
            session = VideoCardTransitionSession(),
            direction = VideoCardTransitionDirection.COLLAPSE,
            skipBackdropEffects = false,
            motionTier = MotionTier.Normal,
            maxBlurRadiusDp = 16f,
            sdkInt = 33
        )

        assertFalse(frame.active)
        assertEquals(1f, frame.scale)
        assertEquals(0f, frame.blurRadiusDp)
        assertEquals(0f, frame.scrimAlpha)
    }

    @Test
    fun expandingMidProgress_appliesScaleAndBlur() {
        val frame = resolveVideoCardTransitionBackdropFrame(
            session = VideoCardTransitionSession(
                phase = VideoCardTransitionPhase.EXPANDING,
                progress = 0.5f
            ),
            direction = VideoCardTransitionDirection.EXPAND,
            skipBackdropEffects = false,
            motionTier = MotionTier.Normal,
            maxBlurRadiusDp = 16f,
            sdkInt = 33
        )

        assertTrue(frame.scale < 1f)
        assertTrue(frame.blurRadiusDp > 0f)
        assertTrue(frame.scrimAlpha > 0f)
    }

    @Test
    fun collapseAtZero_isFullyClear() {
        val frame = resolveVideoCardTransitionBackdropFrame(
            session = VideoCardTransitionSession(
                phase = VideoCardTransitionPhase.COLLAPSING,
                progress = 0f
            ),
            direction = VideoCardTransitionDirection.COLLAPSE,
            skipBackdropEffects = false,
            motionTier = MotionTier.Normal,
            maxBlurRadiusDp = 16f,
            sdkInt = 33
        )

        assertEquals(1f, frame.scale)
        assertEquals(0f, frame.blurRadiusDp)
        assertEquals(0f, frame.scrimAlpha)
    }

    @Test
    fun collapseBlurDecaysFasterThanExpandAtSameProgress() {
        val expandBlur = resolveVideoCardTransitionBackdropFrame(
            session = VideoCardTransitionSession(
                phase = VideoCardTransitionPhase.EXPANDING,
                progress = 0.3f
            ),
            direction = VideoCardTransitionDirection.EXPAND,
            skipBackdropEffects = false,
            motionTier = MotionTier.Normal,
            maxBlurRadiusDp = 16f,
            sdkInt = 33
        ).blurRadiusDp
        val collapseBlur = resolveVideoCardTransitionBackdropFrame(
            session = VideoCardTransitionSession(
                phase = VideoCardTransitionPhase.COLLAPSING,
                progress = 0.3f
            ),
            direction = VideoCardTransitionDirection.COLLAPSE,
            skipBackdropEffects = false,
            motionTier = MotionTier.Normal,
            maxBlurRadiusDp = 16f,
            sdkInt = 33
        ).blurRadiusDp

        assertTrue(collapseBlur < expandBlur)
    }

    @Test
    fun quickReturn_skipsBackdropEffects() {
        val frame = resolveVideoCardTransitionBackdropFrame(
            session = VideoCardTransitionSession(
                phase = VideoCardTransitionPhase.COLLAPSING,
                progress = 0.8f
            ),
            direction = VideoCardTransitionDirection.COLLAPSE,
            skipBackdropEffects = true,
            motionTier = MotionTier.Normal,
            maxBlurRadiusDp = 16f,
            sdkInt = 33
        )

        assertFalse(frame.active)
        assertEquals(0f, frame.blurRadiusDp)
    }

    @Test
    fun reducedMotion_usesScaleWithoutBlur() {
        val frame = resolveVideoCardTransitionBackdropFrame(
            session = VideoCardTransitionSession(
                phase = VideoCardTransitionPhase.EXPANDING,
                progress = 0.6f
            ),
            direction = VideoCardTransitionDirection.EXPAND,
            skipBackdropEffects = false,
            motionTier = MotionTier.Reduced,
            maxBlurRadiusDp = 16f,
            sdkInt = 33
        )

        assertTrue(frame.scale < 1f)
        assertEquals(0f, frame.blurRadiusDp)
        assertTrue(frame.scrimAlpha > 0f)
    }

    @Test
    fun renderEffectBlurMode_requiresApi31AndActiveNormalMotionBlur() {
        assertEquals(
            VideoTransitionBackdropBlurMode.RENDER_EFFECT,
            resolveVideoTransitionBackdropBlurMode(
                blurRadiusDp = 12f,
                motionTier = MotionTier.Normal,
                sdkInt = 31
            )
        )
        assertEquals(
            VideoTransitionBackdropBlurMode.RENDER_EFFECT,
            resolveVideoTransitionBackdropBlurMode(
                blurRadiusDp = 12f,
                motionTier = MotionTier.Enhanced,
                sdkInt = 35
            )
        )
        assertEquals(
            VideoTransitionBackdropBlurMode.COMPOSE_BLUR_FALLBACK,
            resolveVideoTransitionBackdropBlurMode(
                blurRadiusDp = 12f,
                motionTier = MotionTier.Normal,
                sdkInt = 30
            )
        )
        assertEquals(
            VideoTransitionBackdropBlurMode.DISABLED,
            resolveVideoTransitionBackdropBlurMode(
                blurRadiusDp = 12f,
                motionTier = MotionTier.Reduced,
                sdkInt = 35
            )
        )
        assertEquals(
            VideoTransitionBackdropBlurMode.DISABLED,
            resolveVideoTransitionBackdropBlurMode(
                blurRadiusDp = 0.2f,
                motionTier = MotionTier.Normal,
                sdkInt = 35
            )
        )
    }

    @Test
    fun expandedFraction_resolvesSessionPhases() {
        assertEquals(
            VideoCardTransitionPhase.IDLE,
            resolveVideoCardTransitionSessionFromExpandedFraction(0f).phase
        )
        assertEquals(
            VideoCardTransitionPhase.EXPANDED,
            resolveVideoCardTransitionSessionFromExpandedFraction(1f).phase
        )
        assertEquals(
            VideoCardTransitionPhase.COLLAPSING,
            resolveVideoCardTransitionSessionFromExpandedFraction(0.4f).phase
        )
    }

    @Test
    fun backdropEntryGate_requiresUnderlyingSourceAndActiveSession() {
        assertTrue(
            shouldApplyVideoCardTransitionBackdropToEntry(
                cardTransitionEnabled = true,
                session = VideoCardTransitionSession(
                    phase = VideoCardTransitionPhase.EXPANDING,
                    progress = 0.4f
                ),
                entryInvolvesVideoDetail = true,
                entryIsUnderlyingSource = true
            )
        )
        assertFalse(
            shouldApplyVideoCardTransitionBackdropToEntry(
                cardTransitionEnabled = true,
                session = VideoCardTransitionSession(),
                entryInvolvesVideoDetail = true,
                entryIsUnderlyingSource = true
            )
        )
    }
}
