CREATE PROCEDURE GetAvgPriceByCategory(in_cat VARCHAR(50))
BEGIN
    SELECT ROUND(AVG(price), 2) AS avgPrice
    FROM book
    WHERE category = in_cat;
END;