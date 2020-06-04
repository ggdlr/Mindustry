package mindustry.entities.bullet;

import mindustry.gen.*;

public class FlameBulletType extends BulletType{
    public FlameBulletType(float speed, float damage){
        super(speed, damage);
    }

    @Override
    public float range(){
        return 50f;
    }

    @Override
    public void draw(Bulletc b){
    }
}
