create table CATEGORY(
  ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  NAME VARCHAR NOT NULL UNIQUE,
  REQ_NAME VARCHAR NOT NULL UNIQUE,
  DELETED BOOLEAN NOT NULL
);

create table BANNER(
  ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  NAME VARCHAR NOT NULL UNIQUE,
  PRICE DECIMAL(10, 2) NOT NULL,
  CATEGORY_ID BIGINT NOT NULL,
  CONTENT VARCHAR NOT NULL,
  DELETED BOOLEAN NOT NULL,
  FOREIGN KEY(CATEGORY_ID) REFERENCES CATEGORY(ID)
);

create table REQUEST(
    ID BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    BANNER_ID BIGINT NOT NULL,
    USER_AGENT VARCHAR,
    IP_ADDRESS VARCHAR,
    REQ_DATE TIMESTAMP,
    FOREIGN KEY(BANNER_ID) REFERENCES BANNER(ID)
);

