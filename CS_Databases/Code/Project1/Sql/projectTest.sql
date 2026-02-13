-- Create Database
CREATE DATABASE w26drandall3_projectTest;

-- Create Tables and Insert Sample Data
CREATE TABLE Member (
    member_id INT PRIMARY KEY,
    name VARCHAR(50),
    city VARCHAR(50),
    join_date DATE
);

INSERT INTO Member VALUES
(1, 'Alice Johnson', 'Seattle', '2023-01-15'),
(2, 'Brian Smith', 'New York', '2022-11-20'),
(3, 'Catherine Lee', 'Chicago', '2023-02-05'),
(4, 'Daniel Carter', 'Boston', '2023-03-18'),
(5, 'Emily Davis', 'Seattle', '2023-05-09');

CREATE TABLE Book (
    book_id INT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(50),
    category VARCHAR(30),
    price DECIMAL(6,2)
);

INSERT INTO Book VALUES
(101, 'Murder on the Orient Express', 'Agatha Christie', 'Mystery', 12.99),
(102, 'The Great Gatsby', 'F. Scott Fitzgerald', 'Classic', 10.50),
(103, 'The Da Vinci Code', 'Dan Brown', 'Thriller', 14.00),
(104, 'Pride and Prejudice', 'Jane Austen', 'Romance', 9.99),
(105, 'Gone Girl', 'Gillian Flynn', 'Mystery', 13.50),
(106, 'Inferno', 'Dan Brown', 'Thriller', 15.25),
(107, 'To Kill a Mockingbird', 'Harper Lee', 'Classic', 11.75);

CREATE TABLE Borrow (
    borrow_id INT PRIMARY KEY,
    member_id INT,
    book_id INT,
    borrow_date DATE,
    return_date DATE,
    FOREIGN KEY (member_id) REFERENCES Member(member_id),
    FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

INSERT INTO Borrow VALUES
(301, 1, 101, '2025-06-01', '2025-06-10'),
(302, 1, 103, '2025-06-15', '2025-06-30'),
(303, 2, 102, '2025-06-10', '2025-06-24'),
(304, 3, 104, '2025-07-01', '2025-07-20'),
(305, 3, 105, '2025-07-22', '2025-08-02'),
(306, 4, 106, '2025-08-05', NULL),
(307, 5, 107, '2025-09-01', '2025-09-12'),
(308, 5, 101, '2025-09-15', '2025-09-29');
