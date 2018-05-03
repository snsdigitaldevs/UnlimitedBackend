package com.simonschuster.pimsleur.unlimited.integration;

import com.simonschuster.pimsleur.unlimited.controllers.ProductInfoController;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AllPuIsbnProductInfoTest {

    @Autowired
    private ProductInfoController productInfoController;

    @Test
//    @Ignore // do not run this test in ci, we only need this to run locally
    // to see if different product response would cause errors
    public void shouldGetAvailablePracticesOfAllPuISBNs() throws Exception {
        for (String puISBN : PUIsbnList.puISBNs) {
            System.out.println(PUIsbnList.puISBNs.indexOf(puISBN) + 1);
            System.out.println(puISBN + " will run");
            List<Course> courseList = productInfoController.getProductInfo(true, puISBN, "whatever");
            courseList
                    .stream()
                    .flatMap(it->it.getLessons().stream())
                    .map(lesson -> {
                        System.out.println(lesson.getLessonNumber() + "---------");
                        return Long.parseLong(lesson.getLessonNumber());
                    })
                    .collect(Collectors.toList());

            System.out.println(puISBN + " is ok");
            System.out.println();
        }
    }

}