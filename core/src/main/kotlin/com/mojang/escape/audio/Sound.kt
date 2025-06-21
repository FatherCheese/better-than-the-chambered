package com.mojang.escape.audio

import com.mojang.escape.menu.settings.GameSettings
import org.lwjgl.openal.AL10
import org.lwjgl.system.MemoryUtil
import javax.sound.sampled.AudioSystem

class Sound(private val bufferID: Int?) {
    companion object {
        val altar = loadSound("/snd/altar.wav")
        val click1 = loadSound("/snd/click.wav")
        val click2 = loadSound("/snd/click2.wav")
        val hurt = loadSound("/snd/hurt.wav")
        val hurt2 = loadSound("/snd/hurt2.wav")
        val kill = loadSound("/snd/kill.wav")
        val pickup = loadSound("/snd/pickup.wav")
        val shoot = loadSound("/snd/shoot.wav")
        val potion = loadSound("/snd/potion.wav")
        val death = loadSound("/snd/death.wav")

        /**
         * A method to load sound files via file name.
         */
        fun loadSound(fileName: String, clazz: Class<*>? = null): Sound {
            return try {
                // First, we try to get an audio file via an input stream, loaded
                // from a class. We try to first do it from the call-class.
                // If that fails, we try from THIS class.
                val ais = AudioSystem.getAudioInputStream(if (clazz != null) {
                    clazz.getResource(fileName)
                } else {
                    Sound::class.java.getResource(fileName)
                })

                // Then we set up some vals from the AIS. First the format,
                // then the bytes, and finally we allocate memory to the
                // byte sizes. We make sure to 'flip' it, so it's ready to be used.
                val format = ais.format
                val audioBytes = ais.readBytes()
                val buffer = MemoryUtil.memAlloc(audioBytes.size)
                buffer.put(audioBytes)
                buffer.flip()

                // Now we generate a buffer ID and set the data from the vals
                // above this point and then free up the memory used by the buffer.
                // And then finally, we return a new Sound class using the buffer ID.
                // PrintLn showed the files being 44.1hz, 16 bit, and mono. - Cookie (FatherCheese)
                val bufferID = AL10.alGenBuffers()
                AL10.alBufferData(bufferID, AL10.AL_FORMAT_MONO16, buffer, format.sampleRate.toInt())
                MemoryUtil.memFree(buffer)
                return Sound(bufferID)

                // After all of that we have our catch method, for if the try fails.
                // It will print a simple message, then the error message, and finally
                // set the buffer ID to null.
            } catch (e: Exception) {
                println("Failed to load sound ${fileName}:")
                println(e.localizedMessage)
                Sound(null)
            }
        }
    }

    /**
     * A method to play sounds.
     */
    fun play() {
        // First, we check if the buffer ID is null. If so, we return.
        // Then we move on to trying to generate sound sources.
        if (bufferID == null) return
        try {
            // We make a source ID using genSource and set its source to
            // the buffer ID. Then we get a "percentage" of the volume,
            // aka the volume value divided by four. We then play the sound.
            val sourceId = AL10.alGenSources()
            AL10.alSourcei(sourceId, AL10.AL_BUFFER, bufferID)

            val volPercentage = GameSettings.Companion.volume.value / 4.0f
            AL10.alSourcef(sourceId, AL10.AL_GAIN, volPercentage)
            AL10.alSourcePlay(sourceId)

            // Now we make a new thread, and if the sound ID is in a
            // "playing" state, we pause the thread for 100 milliseconds.
            // Once it passes, we delete the source ID.
            Thread {
                while (AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
                    Thread.sleep(100)
                }
                AL10.alDeleteSources(sourceId)
            }.start()

            // After everything we have our catch block, which will
            // just log an error if the function fails.
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}