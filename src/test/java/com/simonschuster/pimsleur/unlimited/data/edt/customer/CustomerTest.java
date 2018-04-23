package com.simonschuster.pimsleur.unlimited.data.edt.customer;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class CustomerTest {

    @Test
    public void shouldCollectProductCodes() {
        Customer customer = new Customer();

        customer.setCustomersOrders(asList(
                createOrder("SW 9781508243359"),
                createOrder("SW 9781508243311"),
                createOrder("SW 9781508243312")
        ));

        List<String> codes = customer.getProductCodes();
        assertThat(codes, containsInAnyOrder(
                "SW 9781508243359",
                "SW 9781508243311",
                "SW 9781508243312"));
    }

    private CustomersOrder createOrder(String productsModel) {
        CustomersOrder customersOrder = new CustomersOrder();
        OrdersProduct ordersProduct = new OrdersProduct();
        ordersProduct.setProductsModel(productsModel);
        customersOrder.setOrdersProducts(asList(ordersProduct));
        return customersOrder;
    }

}