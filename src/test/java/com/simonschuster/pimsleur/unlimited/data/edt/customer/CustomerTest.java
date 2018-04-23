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
                createOrder("978-1-5082-4335-9"),
                createOrder("978-1-5082-4331-1"),
                createOrder("978-1-5082-4331-2")
        ));

        List<String> codes = customer.getProductCodes();
        assertThat(codes, containsInAnyOrder(
                "9781508243359",
                "9781508243311",
                "9781508243312"));
    }

    private CustomersOrder createOrder(String productsModel) {
        CustomersOrder customersOrder = new CustomersOrder();
        OrdersProduct ordersProduct = new OrdersProduct();
        Product product = new Product();
        product.setIsbn13(productsModel);
        ordersProduct.setProduct(product);
        customersOrder.setOrdersProducts(asList(ordersProduct));
        return customersOrder;
    }

}