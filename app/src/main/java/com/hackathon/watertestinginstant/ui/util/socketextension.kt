package com.hackathon.watertestinginstant.ui.util

import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.Closeable
import kotlin.coroutines.resume

internal suspend inline fun <T : Closeable?, R> T.useCancellably(
    crossinline block: (T) -> R
): R = suspendCancellableCoroutine { cont ->
    cont.invokeOnCancellation { this?.close() }
    cont.resume(use(block))
}