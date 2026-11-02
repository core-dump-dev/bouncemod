package core_dump.bouncemod.client.sound;

import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class BounceMusicSound extends AbstractSoundInstance {

    private static final Identifier ID =
            Identifier.fromNamespaceAndPath("bounce-mod", "bounce_music");

    public BounceMusicSound() {
        super(ID, SoundSource.MUSIC, RandomSource.create());
    }

    @Override public boolean isLooping() { return true; }
    @Override public boolean isRelative() { return true; }
    @Override public float getVolume() { return BounceState.config.musicVolume; }
}