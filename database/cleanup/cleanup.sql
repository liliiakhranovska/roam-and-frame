BEGIN;

TRUNCATE TABLE
    cart.cart_items,
    orders.order_items,
    orders.orders,
    payment.payments,
    shipping.shipments,
    inventory.stock_items,
    catalog.products,
    catalog.categories,
    customer.customers;

COMMIT;