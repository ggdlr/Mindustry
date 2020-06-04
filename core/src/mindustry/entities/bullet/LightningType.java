package mindustry.entities.bullet;

import mindustry.content.Fx;
import mindustry.entities.Lightning;
import mindustry.gen.Bulletc;
import mindustry.graphics.Pal;

import static mindustry.Vars.state;

public class LightningType extends LightningBulletType{
    public LightningType(float speed, float damage){
        despawnEffect = Fx.hitBulletSmall;
    }

    @Override
    public float range(){
        return 70f;
    }

    @Override
    public void draw(Bulletc b){
    }

    @Override
    public void init(Bulletc b){
        Lightning.create(b.team(), Pal.lancerLaser, damage * (b.owner().isLocal() ? state.rules.playerDamageMultiplier : 1f), b.x(), b.y(), b.rotation(), 30);
    }
}
