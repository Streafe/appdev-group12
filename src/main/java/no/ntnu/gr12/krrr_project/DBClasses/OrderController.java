package no.ntnu.gr12.krrr_project.DBClasses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Controller class for the orders. Controls the HTTP requests and parses it from the OrderService class.
 *
 * @author Anders M. H. Frostrud
 */
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * Returns the Order list through the /orders mapping.
     * @return the orders as a list
     */
    @RequestMapping("/orders")
    public List<Order> getOrder() {
        return orderService.getOrders();
    }

    /**
     * Returns a specific order through the /orders mapping
     * @param id the id of the order to be requested
     * @return the specific order
     */
    @RequestMapping("/orders/{id}")
    public Order getOrder(@PathVariable String id) {
        return orderService.getOrder(id);
    }

    /**
     * Adds an order to the order list in orderService class through the /orders mapping
     * @param order the order to be added
     */
    @RequestMapping(method = RequestMethod.POST, value = "/orders")
    public void addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
    }

    /**
     * Updates a specific order with a new order through the mapping.
     * @param order the updated order
     * @param id the id of the order to be updated
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/orders/{id}")
    public void updateOrder(@RequestBody Order order, @PathVariable String id) {
        orderService.updateOrder(id, order);
    }

    /**
     * Deletes a specific order through the mapping
     * @param id the id of the order to be deleted
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/orders/{id}")
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }
}