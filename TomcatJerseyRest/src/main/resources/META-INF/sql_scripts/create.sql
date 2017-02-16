CREATE TABLE country (
    Code char(3) NOT NULL
    , Name char(52) NOT NULL
    , PRIMARY KEY (Code)
);
CREATE TABLE city (
    ID int NOT NULL
    , Name char(35) NOT NULL
    , CountryCode char(3) NOT NULL
    , PRIMARY KEY (ID)
    , FOREIGN KEY (CountryCode) REFERENCES country(Code)
);
