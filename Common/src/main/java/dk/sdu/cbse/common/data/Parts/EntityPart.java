package dk.sdu.cbse.common.data.Parts;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.VisualGameData;

public interface EntityPart {
    void process(VisualGameData vgData, Entity entity);
}
