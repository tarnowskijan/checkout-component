merge into PRODUCT KEY(id) values
    ('milk', 'Milk 1l'),
    ('sugar', 'Sugar 1kg'),
    ('water', 'Water 1.5l'),
    ('eggs', 'Eggs 10pcs'),
    ('bread', 'Bread'),
    ('coffee', 'Coffee 1kg');

merge into PRODUCT_PRICING KEY(product_id, currency) values
    ('milk', '1.05', 'USD'),
    ('sugar', '2.15', 'USD'),
    ('water', '0.5', 'USD'),
    ('eggs', '3.00', 'USD'),
    ('bread', '1.5', 'USD'),
    ('coffee', '5.50', 'USD');

merge into SPECIAL_PRICE_SINGLE_PRODUCT_DISCOUNT KEY(product_id, applicable_quantity, price, currency) values
    ('milk', '5', '4.9', 'USD'),
    ('sugar', '10', '20', 'USD'),
    ('bread', '3', '3.99', 'USD'),
    ('coffee', '100', '480', 'USD');