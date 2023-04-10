
    drop table if exists customer;

    drop table if exists phone;

    create table customer (
       id varchar(36) not null,
        created_date datetime(6),
        name varchar(255),
        update_date datetime(6),
        version integer,
        primary key (id)
    ) engine=InnoDB;

    create table phone (
       id varchar(36) not null,
        created_date datetime(6),
        imei varchar(255) not null,
        phone_name varchar(50) not null,
        phone_style smallint not null,
        price decimal(38,2) not null,
        quantity_on_hand integer,
        update_date datetime(6),
        version integer,
        primary key (id)
    ) engine=InnoDB;
