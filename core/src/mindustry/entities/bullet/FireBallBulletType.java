package mindustry.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class FireBallBulletType extends BulletType{
    public FireBallBulletType(float speed, float damage){
        super(speed, damage);
    }

    @Override
    public void init(Bulletc b){
        b.vel().setLength(0.6f + Mathf.random(2f));
    }

    @Override
    public void draw(Bulletc b){
        Draw.color(Pal.lightFlame, Pal.darkFlame, Color.gray, b.fin());
        Fill.circle(b.x(), b.y(), 3f * b.fout());
        Draw.reset();
    }

    @Override
    public void update(Bulletc b){
        if(Mathf.chance(0.04 * Time.delta())){
            Tile tile = world.tileWorld(b.x(), b.y());
            if(tile != null){
                Fires.create(tile);
            }
        }

        if(Mathf.chance(0.1 * Time.delta())){
            Fx.fireballsmoke.at(b.x(), b.y());
        }

        if(Mathf.chance(0.1 * Time.delta())){
            Fx.ballfire.at(b.x(), b.y());
        }
    }
}
