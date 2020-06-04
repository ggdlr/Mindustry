package mindustry.entities.bullet;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.graphics.*;

import static mindustry.Vars.world;

public class MeltDownLaserType extends LaserBulletType{
    Color tmpColor = new Color();
    Color[] colors = {Color.valueOf("ec745855"), Color.valueOf("ec7458aa"), Color.valueOf("ff9c5a"), Color.white};
    float[] tscales = {1f, 0.7f, 0.5f, 0.2f};
    float[] strokes = {2f, 1.5f, 1f, 0.3f};
    float[] lenscales = {1f, 1.12f, 1.15f, 1.17f};
    float length = 220f;
    public MeltDownLaserType(float speed, float damage){
        shootEffect = Fx.shootSmall;
        smokeEffect = Fx.shootSmallSmoke;
    }
    @Override
    public void update(Bulletc b){
        if(b.timer(1, 5f)){
            Damage.collideLine(b, b.team(), hitEffect, b.x(), b.y(), b.rotation(), length, true);
        }
        Effects.shake(1f, 1f, b.x(), b.y());
    }

    @Override
    public void hit(Bulletc b, float hitx, float hity){
        hitEffect.at(hitx, hity, colors[2]);
        if(Mathf.chance(0.4)){
            Fires.create(world.tileWorld(hitx + Mathf.range(5f), hity + Mathf.range(5f)));
        }
    }

    @Override
    public void draw(Bulletc b){
        float baseLen = (length) * b.fout();

        Lines.lineAngle(b.x(), b.y(), b.rotation(), baseLen);
        for(int s = 0; s < colors.length; s++){
            Draw.color(tmpColor.set(colors[s]).mul(1f + Mathf.absin(Time.time(), 1f, 0.1f)));
            for(int i = 0; i < tscales.length; i++){
                Tmp.v1.trns(b.rotation() + 180f, (lenscales[i] - 1f) * 35f);
                Lines.stroke((9f + Mathf.absin(Time.time(), 0.8f, 1.5f)) * b.fout() * strokes[s] * tscales[i]);
                Lines.lineAngle(b.x() + Tmp.v1.x, b.y() + Tmp.v1.y, b.rotation(), baseLen * lenscales[i], CapStyle.none);
            }
        }

        Tmp.v1.trns(b.rotation(), baseLen * 1.1f);

        Drawf.light(b.x(), b.y(), b.x() + Tmp.v1.x, b.y() + Tmp.v1.y, 40, Color.orange, 0.7f);
        Draw.reset();
    }
}
