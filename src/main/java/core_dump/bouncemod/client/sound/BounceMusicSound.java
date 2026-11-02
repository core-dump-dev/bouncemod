package core_dump.bouncemod.client.sound;

import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class BounceMusicSound extends AbstractTickableSoundInstance {

    private static final Identifier ID =
            Identifier.fromNamespaceAndPath("bounce-mod", "bounce_music");

    // В 26.3 вместо SoundEvent.of(...) используется createVariableRangeEvent(...)
    private static final SoundEvent EVENT =
            SoundEvent.createVariableRangeEvent(ID);

    public BounceMusicSound() {
        super(EVENT, SoundSource.MUSIC, RandomSource.create());
    }

    @Override
    public void tick() {
        // Каждый тик обновляем громкость — SoundManager применит новое значение сразу
        this.volume = BounceState.config.musicVolume;
    }

    @Override public boolean isLooping() { return true; }
    @Override public boolean isRelative() { return true; }
    @Override public float getVolume() { return BounceState.config.musicVolume; }
}