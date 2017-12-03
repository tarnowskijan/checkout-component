create table if not exists PRODUCT ( ID varchar(30), NAME varchar(500) not null, constraint PRODUCT_PRIMARY_KEY primary key (id) );

create table if not exists PRODUCT_PRICING (PRODUCT_ID varchar(30), PRICE decimal(20, 4) not null, CURRENCY char(3) not null, constraint PRODUCT_PRICING_PRIMARY_KEY primary key (PRODUCT_ID, CURRENCY), constraint PRODUCT_PRICING_FOREIGN_KEY foreign key (PRODUCT_ID) references PRODUCT(ID));

create table if not exists SPECIAL_PRICE_SINGLE_PRODUCT_DISCOUNT (PRODUCT_ID varchar(30), APPLICABLE_QUANTITY int not null, PRICE decimal(20, 4) not null, CURRENCY char(3) not null, constraint SPECIAL_PRICE_SINGLE_PRODUCT_DISCOUNT_FOREIGN_KEY foreign key (PRODUCT_ID) references PRODUCT(ID));