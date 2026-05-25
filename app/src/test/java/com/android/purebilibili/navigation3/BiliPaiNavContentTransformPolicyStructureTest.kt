package com.android.purebilibili.navigation3

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class BiliPaiNavContentTransformPolicyStructureTest {

    @Test
    fun disabledVideoDirectionalReturnKeepsTargetContentVisibleImmediately() {
        val source = contentTransformPolicySource()
        val returnFunctionStart = source.indexOf("private fun disabledVideoDirectionReturnTransform")
        val returnFunctionEnd = source.length
        val returnFunction = source.substring(returnFunctionStart, returnFunctionEnd)

        assertTrue(returnFunction.contains("return EnterTransition.None togetherWith"))
    }

    @Test
    fun spaceForwardUsesLightSlideAndFade() {
        val source = contentTransformPolicySource()

        assertTrue(source.contains("BiliPaiNavRouteTransition.SPACE_FORWARD"))
        assertTrue(source.contains("private fun spaceForwardTransform()"))
        assertTrue(source.contains("initialOffsetX = { width -> width / 8 }"))
        assertTrue(source.contains("fadeIn(animationSpec = tween(NAV3_SPACE_FORWARD_MILLIS))"))
    }

    private fun contentTransformPolicySource(): String {
        return listOf(
            File("app/src/main/java/com/android/purebilibili/navigation3/BiliPaiNavContentTransformPolicy.kt"),
            File("src/main/java/com/android/purebilibili/navigation3/BiliPaiNavContentTransformPolicy.kt")
        ).first { it.exists() }.readText()
    }
}
