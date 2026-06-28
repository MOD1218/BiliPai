package com.android.purebilibili.core.ui

import com.android.purebilibili.core.theme.AndroidNativeVariant
import com.android.purebilibili.core.theme.UiPreset

fun resolveAdaptivePullToRefreshRenderer(
    uiPreset: UiPreset,
    androidNativeVariant: AndroidNativeVariant
): PresetPrimitiveRenderer = resolvePresetPrimitiveRenderer(
    uiPreset = uiPreset,
    androidNativeVariant = androidNativeVariant
)

fun resolveMiuixPullToRefreshTexts(): List<String> = listOf(
    "下拉刷新...",
    "松手刷新",
    "正在刷新...",
    "刷新完成",
)