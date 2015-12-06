Use amazon;

Drop TABLE books;

CREATE TABLE books(
  id           VARCHAR(4),
  title        VARCHAR(64),
  author       VARCHAR(32),
  price        FLOAT,
  year         INT,
  description  VARCHAR(128),
  inventory    INT
);

INSERT INTO books VALUES('101','How to earn million in a week?',
'Albert Leng',999.99, 2008,'Best Selling Book of the year',10);

INSERT INTO books VALUES('201','My Early Years: Growing up on 7',
'Mary Brown',30.75,1995,'What a cool book.',10);

INSERT INTO books VALUES('202','Web Servers for Fun and Profit',
'Jeeves',40.75,2000,'',10);

INSERT INTO books VALUES('203','Web Components for Web Developers',
'Masterson Webster',27.75,2000,'The best web development book.',10);

INSERT INTO books VALUES('205','From Oak to Java: The Revolution of a Language',
'Novation Kevin',10.75,1998,'Best selling Java book year 1998',10);

INSERT INTO books VALUES('206','Java Intermediate Bytecodes',
'Gosling James',30.95,2000,'', 10);

INSERT INTO books VALUES('207','The Green Project: Programming for Consumer Devices',
'Thrilled Ben',30.50,1998,'',10);

INSERT INTO books VALUES('208','Duke: A Biography of the Java Evangelist',
'Tru Itzal',45.50,2001,'',10);