package com.simonschuster.pimsleur.unlimited.services.course;

import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.Course;
import com.simonschuster.pimsleur.unlimited.data.dto.productinfo.PcmReadings;
import com.simonschuster.pimsleur.unlimited.data.edt.customer.OrdersProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PcmReadingsService {

    public void addReadingsToCourses(List<Course> courses, List<OrdersProduct> ordersProducts) {
        courses.forEach(course -> {
            course.setReadings(new PcmReadings("x.pdf", null));
        });
    }
}
