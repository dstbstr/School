CREATE PROCEDURE GetAuthorPairs()
BEGIN
  WITH Isbns AS (
    SELECT ISBN FROM book_author
    GROUP BY ISBN
    HAVING COUNT(authorID) > 1
  ),
  AllAuthors AS (
    SELECT CONCAT(firstName, ' ', lastName) AS authorName
    FROM book_author NATURAL JOIN author 
    WHERE ISBN IN (
      SELECT * FROM Isbns
    )
  )
  SELECT DISTINCT CONCAT(Lhs.authorName, ' and ', Rhs.authorName) AS authors
  FROM AllAuthors AS Lhs, AllAuthors AS Rhs
  WHERE Lhs.authorName < Rhs.authorName
  ORDER BY authors;
END