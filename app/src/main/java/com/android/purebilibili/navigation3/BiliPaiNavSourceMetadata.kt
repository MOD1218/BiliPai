package com.android.purebilibili.navigation3

internal enum class BiliPaiNavCardSourceDirection {
    NONE,
    SOURCE_LEFT,
    SOURCE_RIGHT
}

internal data class BiliPaiNavSourceMetadata(
    val sourceKey: String? = null,
    val sourceRoute: String? = null,
    val clickedBoundsRecorded: Boolean = false,
    val cardFullyVisible: Boolean = false,
    val cardSourceDirection: BiliPaiNavCardSourceDirection = BiliPaiNavCardSourceDirection.NONE
) {
    val sharedTransitionEntryReady: Boolean
        get() = clickedBoundsRecorded

    val sharedTransitionReady: Boolean
        get() = clickedBoundsRecorded && cardFullyVisible
}

/**
 * Resolve left/right origin of a dual-column video card.
 *
 * Visibility is intentionally not required: when card morph is disabled we still need a
 * reliable left/right exit direction even if the card sits under the top chrome.
 * Single-column (story) cards stay undirected so they keep vertical motion semantics.
 *
 * [cardFullyVisible] remains in the signature for call-site compatibility with shared-element
 * gates; direction itself only needs a recorded dual-column center X.
 */
@Suppress("UNUSED_PARAMETER")
internal fun resolveBiliPaiNavCardSourceDirection(
    clickedBoundsRecorded: Boolean,
    cardFullyVisible: Boolean,
    isSingleColumnCard: Boolean,
    normalizedCenterX: Float?
): BiliPaiNavCardSourceDirection {
    if (!clickedBoundsRecorded || isSingleColumnCard) {
        return BiliPaiNavCardSourceDirection.NONE
    }
    val centerX = normalizedCenterX ?: return BiliPaiNavCardSourceDirection.NONE
    // Dual-column feed: left column ~0.25, right ~0.75. Use mid-screen split so both sides
    // always get a directed enter/exit when morph animations are off.
    return if (centerX < 0.5f) {
        BiliPaiNavCardSourceDirection.SOURCE_LEFT
    } else {
        BiliPaiNavCardSourceDirection.SOURCE_RIGHT
    }
}

internal fun resolveBiliPaiNavSourceMetadata(
    sourceKey: String? = null,
    sourceRoute: String? = null,
    clickedBoundsRecorded: Boolean,
    cardFullyVisible: Boolean,
    cardSourceDirection: BiliPaiNavCardSourceDirection = BiliPaiNavCardSourceDirection.NONE
): BiliPaiNavSourceMetadata {
    return BiliPaiNavSourceMetadata(
        sourceKey = sourceKey,
        sourceRoute = normalizeBiliPaiVideoSourceRoute(sourceRoute),
        clickedBoundsRecorded = clickedBoundsRecorded,
        cardFullyVisible = cardFullyVisible,
        cardSourceDirection = cardSourceDirection
    )
}
