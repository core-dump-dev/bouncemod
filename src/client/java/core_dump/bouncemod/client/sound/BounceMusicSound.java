package core_dump.bouncemod.client.sound;

import core_dump.bouncemod.client.BounceState;
import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class BounceMusicSound extends AbstractSoundInstance {

    private static final Identifier ID = Identifier.of("bounce-mod", "bounce_music");

    public BounceMusicSound() {
        super(ID, SoundCategory.MUSIC, Random.create());
        this.repeat = true;
        this.repeatDelay = 0;
        this.relative = true;
        this.attenuationType = SoundInstance.AttenuationType.NONE;
    }

    @Override public float getVolume() { return BounceState.config.musicVolume; }
    @Override public boolean canRepeat() { return true; }
}