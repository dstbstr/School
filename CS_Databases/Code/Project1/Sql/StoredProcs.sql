DROP FUNCTION IF EXISTS GetAvgPriceByCategory;

DELIMITER $$
CREATE FUNCTION GetAvgPriceByCategory(in_cat VARCHAR(50))
RETURNS DECIMAL(10, 2)
DETERMINISTIC
BEGIN
  DECLARE avgPrice DECIMAL(10, 2);

  SELECT ROUND(AVG(price), 2) INTO avgPrice
  FROM book
  WHERE category = in_cat;
    
  RETURN avgPrice;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS GetAuthorPairs;

DELIMITER $$
CREATE PROCEDURE GetAuthorPairs()
BEGIN
  WITH Isbns AS (
    SELECT ISBN FROM book_author
    GROUP BY ISBN
    HAVING COUNT(authorID) > 1
  ),
  AllAuthors AS (
    SELECT ISBN, CONCAT(firstName, ' ', lastName) AS authorName
    FROM book_author NATURAL JOIN author 
    WHERE ISBN IN (
      SELECT ISBN FROM Isbns
    )
  )
  SELECT DISTINCT CONCAT(Lhs.authorName, ' and ', Rhs.authorName) AS authors
  FROM AllAuthors AS Lhs, AllAuthors AS Rhs
  WHERE Lhs.authorName < Rhs.authorName AND Lhs.ISBN = Rhs.ISBN
  ORDER BY authors;

END $$
DELIMITER ;