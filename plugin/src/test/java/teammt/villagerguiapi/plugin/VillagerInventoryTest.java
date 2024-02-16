package teammt.villagerguiapi.plugin;

import static teammt.testingagent.testing.Assertions.assertNotNull;

import teammt.testingagent.annotations.Test;
import teammt.villagerguiapi.api.AdapterLoader;

public class VillagerInventoryTest {
    @Test
    public void checkClassInitializations() {
        assertNotNull(AdapterLoader.getAdapterClass());
    }
}
