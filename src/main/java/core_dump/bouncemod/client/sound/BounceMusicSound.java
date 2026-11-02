package core_dump.bouncemod.client.sound;

import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.RandomSource;

public class BounceMusicSound extends AbstractSoundInstance {

    private static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("bounce-mod", "bounce_music");

    public BounceMusicSound() {
        super(ID, SoundCategory.MUSIC, RandomSource.create());
    }

    @Override public boolean isLooping() { return true; }
    @Override public boolean isRelative() { return true; }
    @Override public SoundInstance.Attenuation getAttenuation() { return SoundInstance.Attenuation.NONE; }
    @Override public int getDelay() { return 0; }
    @Override public float getVolume() { return BounceState.config.musicVolume; }
    @Override public float getPitch() { return 1.0f; }
}