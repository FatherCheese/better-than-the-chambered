package com.mojang.escape.audio

import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC
import org.lwjgl.openal.ALC10
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import java.nio.IntBuffer

class AudioEngine {
    private var device: Long = 0
    private var context: Long = 0

    fun initialize(): Boolean {
        try {
            device = ALC10.alcOpenDevice(null as ByteBuffer?)
            if (device == MemoryUtil.NULL) {
                println("Failed to open the default OpenAL device.")
                return false
            }

            context = ALC10.alcCreateContext(device, null as IntBuffer?)
            if (context == MemoryUtil.NULL) {
                println("Failed to create OpenAL context.")
                ALC10.alcCloseDevice(device)
                return false
            }

            if (!ALC10.alcMakeContextCurrent(context)) {
                println("Failed to make the OpenAL context current.")
                cleanup()
                return false
            }

            AL.createCapabilities(ALC.createCapabilities(device))

            println("OpenAL initialized successfully.")
            return true
        } catch (e: Exception) {
            println("Failed to initialize OpenAL:")
            println(e.localizedMessage)
            cleanup()
            return false
        }
    }

    fun cleanup() {
        if (context != MemoryUtil.NULL) {
            ALC10.alcMakeContextCurrent(MemoryUtil.NULL)
            ALC10.alcDestroyContext(context)
            context = MemoryUtil.NULL
        }

        if (device != MemoryUtil.NULL) {
            ALC10.alcCloseDevice(device)
            device = MemoryUtil.NULL
        }
    }
}