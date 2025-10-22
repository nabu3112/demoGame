package object;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ManageBuff {
    private List<Buff> buffs;

    public ManageBuff() {
        buffs = new ArrayList<>();
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }

    public void addBuff(double xBlock, double yBlock, double widthBlock, double heightBlock, String bufftype) {
        buffs.add(new Buff(xBlock, yBlock, widthBlock, heightBlock,10, 3, 0, 1, bufftype));
    }

    public void addBuffOnScene(GraphicsContext gc, double xPaddle, double yPaddle,
                               double widthPadle, double heightPaddle, ManageBall manageBall) {
        for (int i = buffs.size() - 1; i >= 0; i--) {
            Buff buff = buffs.get(i);
            if (buff.updateBuff(xPaddle, yPaddle, widthPadle, heightPaddle, manageBall)) {
                buffs.remove(i);
            } else {
                buff.addOnScene(gc);
            }
        }
    }

}
