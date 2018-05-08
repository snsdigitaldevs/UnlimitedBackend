package com.simonschuster.pimsleur.unlimited.utils.practices;

import com.simonschuster.pimsleur.unlimited.data.dto.practices.PracticesInUnit;
import org.junit.Test;

import java.util.List;


import static com.simonschuster.pimsleur.unlimited.utils.SkillUtil.getSkillsByIsbn;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SkillUtilTest {
    @Test
    public void shouldGetSkills() throws Exception {
        List<PracticesInUnit> practicesInUnits = getSkillsByIsbn("9781508243328");
        assertThat(practicesInUnits.size(), is(30));

        List<PracticesInUnit> noPracticesInUnits = getSkillsByIsbn("9781508243327");
        assertThat(noPracticesInUnits.size(), is(0));
    }
}
