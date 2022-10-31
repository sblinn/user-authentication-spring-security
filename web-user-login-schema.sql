DROP DATABASE IF EXISTS UserLogin;

CREATE DATABASE UserLogin;

USE UserLogin;

CREATE TABLE Users (
    username VARCHAR(50) PRIMARY KEY, 
    `password` VARCHAR(100) NOT NULL, 
    enabled BOOLEAN NOT NULL DEFAULT 1
);

CREATE TABLE Authorities (
	`authority` VARCHAR(25) PRIMARY KEY
);

CREATE TABLE USER_AUTHORITIES (
	username VARCHAR(50) NOT NULL,
    authority VARCHAR(25),
    CONSTRAINT fk_username FOREIGN KEY (username) 
		REFERENCES Users (username),
	CONSTRAINT fk_authority FOREIGN KEY (authority) 
		REFERENCES Authorities (authority)
);


-- SAMPLE DATA
    
insert into Authorities (authority)
	values ('ROLE_USER');
insert into Authorities (authority)
	values ('ROLE_ADMIN');
    

-- VIEW TABLES

select * from Users;
select * from Authorities;
select * from User_Authorities;


DELETE FROM users WHERE username = 'user';

