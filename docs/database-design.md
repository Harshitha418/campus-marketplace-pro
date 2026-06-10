USER
id
name
email
password
role
created_at


PRODUCT
id
title
description
price
quantity
category
seller_id
created_at
image_url


CART
id
user_id


CART ITEM
id
cart_id
product_id
quantity


ORDER
id
user_id
total_price
status
created_at


ORDER ITEM
id
order_id
product_id
quantity
price


WISHLIST
id
user_id
product_id


REVIEW
id
user_id
product_id
rating
comment