package com.android.purebilibili.core.ui.adaptive

/**
 * 根据设备算力信号解析视频开合过渡可用的 [MotionTier]。
 *
 * 目标：在「开启卡片过渡 + 关闭预测返回」时，高低端机走同一套视觉语义，
 * 但低端自动降掉全屏 GPU 模糊等重代价，把掉帧差收窄到可接受范围。
 *
 * 信号优先级（任一命中即降到 [MotionTier.Reduced]）：
 * 1. [isLowRamDevice]（系统 Go/低内存设备）
 * 2. 堆内存 class 很小（常见入门机 ≤192MB）
 * 3. 核心数很少且内存 class 仍偏低（旧四核 + ≤256MB）
 *
 * 高端信号同时满足时升到 [MotionTier.Enhanced]（更大模糊半径）；
 * 其余手机保持 [MotionTier.Normal]（中等模糊预算）。
 */
internal fun resolveDeviceMotionCapabilityTier(
    isLowRamDevice: Boolean,
    memoryClassMb: Int,
    cpuCores: Int = Runtime.getRuntime().availableProcessors(),
): MotionTier {
    if (isLowRamDevice) return MotionTier.Reduced
    val safeMemoryClass = memoryClassMb.coerceAtLeast(0)
    val safeCpuCores = cpuCores.coerceAtLeast(0)
    if (safeMemoryClass in 1..192) return MotionTier.Reduced
    if (safeCpuCores in 1..4 && safeMemoryClass in 1..256) return MotionTier.Reduced
    if (safeMemoryClass >= 384 && (safeCpuCores == 0 || safeCpuCores >= 6)) {
        return MotionTier.Enhanced
    }
    return MotionTier.Normal
}

/**
 * 视频卡片开合景深最终生效的 motion tier。
 * 系统「减弱动画」永远优先；否则使用设备能力档。
 */
internal fun resolveVideoCardTransitionMotionTier(
    reduceMotion: Boolean,
    deviceCapabilityTier: MotionTier,
): MotionTier {
    if (reduceMotion) return MotionTier.Reduced
    return deviceCapabilityTier
}
