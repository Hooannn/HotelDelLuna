-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: mysql:3306
-- Generation Time: Nov 27, 2023 at 03:08 PM
-- Server version: 8.2.0
-- PHP Version: 8.2.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `default`
--

-- --------------------------------------------------------

--
-- Table structure for table `floors`
--

CREATE TABLE `floors` (
  `id` int NOT NULL,
  `num` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `floors`
--

INSERT INTO `floors` (`id`, `num`) VALUES
(1, 1),
(2, 2),
(3, 3);

-- --------------------------------------------------------

--
-- Table structure for table `invoices`
--

CREATE TABLE `invoices` (
  `id` int NOT NULL,
  `checkInTime` varchar(255) DEFAULT NULL,
  `checkOutTime` varchar(255) DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `customerName` varchar(255) DEFAULT NULL,
  `room` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `invoices`
--

INSERT INTO `invoices` (`id`, `checkInTime`, `checkOutTime`, `total`, `customerName`, `room`) VALUES
(1, '2023-11-28T21:55:31', '2023-11-30T21:55:47', 17281600.00, 'aa', 2);

-- --------------------------------------------------------

--
-- Table structure for table `reservations`
--

CREATE TABLE `reservations` (
  `id` int NOT NULL,
  `checkInTime` varchar(255) DEFAULT NULL,
  `checkOutTime` varchar(255) DEFAULT NULL,
  `customerName` varchar(255) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `customerCount` int DEFAULT NULL,
  `status` enum('OPENING','CLOSED') DEFAULT NULL,
  `room` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `reservations`
--

INSERT INTO `reservations` (`id`, `checkInTime`, `checkOutTime`, `customerName`, `note`, `customerCount`, `status`, `room`) VALUES
(1, '2023-11-27T21:45:30', NULL, 'KHAI HOAN', 'Hey', 2, 'CLOSED', 1),
(2, '2023-11-28T21:55:31', '2023-11-27T21:55:54.934890', 'aa', 'aaaaa', 5, 'CLOSED', 2);

-- --------------------------------------------------------

--
-- Table structure for table `rooms`
--

CREATE TABLE `rooms` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` int DEFAULT NULL,
  `floor` int DEFAULT NULL,
  `status` enum('AVAILABLE','OCCUPIED','MAINTENANCE') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `rooms`
--

INSERT INTO `rooms` (`id`, `name`, `type`, `floor`, `status`) VALUES
(1, 'A001', 4, 1, 'AVAILABLE'),
(2, 'A002', 6, 1, 'AVAILABLE'),
(3, 'A003', 4, 1, 'AVAILABLE'),
(4, 'A004', 4, 1, 'AVAILABLE'),
(5, 'A005', 6, 1, 'AVAILABLE'),
(6, 'A006', 4, 1, 'AVAILABLE'),
(7, 'A007', 4, 1, 'AVAILABLE'),
(8, 'A008', 6, 1, 'AVAILABLE'),
(9, 'B00p1', 4, 2, 'AVAILABLE'),
(10, 'B002', 4, 2, 'AVAILABLE'),
(11, 'B003', 4, 2, 'AVAILABLE'),
(12, 'B00pp4', 6, 2, 'AVAILABLE'),
(13, 'B005', 6, 2, 'AVAILABLE'),
(14, 'B00p6', 4, 2, 'AVAILABLE'),
(15, 'Ac001', 4, 2, 'AVAILABLE'),
(16, 'Aa0z01', 4, 3, 'AVAILABLE'),
(17, 'A0xpasdsd01', 5, 3, 'AVAILABLE'),
(18, 'Adz001', 5, 3, 'AVAILABLE'),
(19, 'Az0s1', 5, 3, 'AVAILABLE'),
(20, 'Axzs001', 4, 3, 'AVAILABLE'),
(21, 'A0wcz01', 5, 3, 'AVAILABLE'),
(22, 'A0ssd01', 4, 3, 'AVAILABLE'),
(23, 'A0esud01', 5, 3, 'AVAILABLE'),
(24, 'A0sld01', 4, 3, 'AVAILABLE'),
(25, 'A0h0ds1', 5, 3, 'AVAILABLE'),
(26, 'A0posda01', 5, 3, 'AVAILABLE'),
(27, 'N21', 4, 1, 'AVAILABLE'),
(28, 'VIP001', 4, 1, 'AVAILABLE'),
(29, 'V009', 4, 1, 'AVAILABLE');

-- --------------------------------------------------------

--
-- Table structure for table `room_types`
--

CREATE TABLE `room_types` (
  `id` int NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pricePerHour` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `room_types`
--

INSERT INTO `room_types` (`id`, `name`, `pricePerHour`) VALUES
(4, 'VIP', 50000.00),
(5, 'Thường', 50000.00),
(6, 'Super Vip', 50000.00);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int NOT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','STAFF','MANAGER','GUEST') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `fullName`, `username`, `password`, `role`) VALUES
(1, 'KHAI HOAN', 'admin', '123456', 'ADMIN');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `floors`
--
ALTER TABLE `floors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `num` (`num`);

--
-- Indexes for table `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`),
  ADD KEY `room` (`room`);

--
-- Indexes for table `reservations`
--
ALTER TABLE `reservations`
  ADD PRIMARY KEY (`id`),
  ADD KEY `room` (`room`);

--
-- Indexes for table `rooms`
--
ALTER TABLE `rooms`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `type` (`type`),
  ADD KEY `floor` (`floor`);

--
-- Indexes for table `room_types`
--
ALTER TABLE `room_types`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `floors`
--
ALTER TABLE `floors`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `invoices`
--
ALTER TABLE `invoices`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `reservations`
--
ALTER TABLE `reservations`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `rooms`
--
ALTER TABLE `rooms`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `room_types`
--
ALTER TABLE `room_types`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`room`) REFERENCES `rooms` (`id`);

--
-- Constraints for table `reservations`
--
ALTER TABLE `reservations`
  ADD CONSTRAINT `reservations_ibfk_1` FOREIGN KEY (`room`) REFERENCES `rooms` (`id`);

--
-- Constraints for table `rooms`
--
ALTER TABLE `rooms`
  ADD CONSTRAINT `rooms_ibfk_1` FOREIGN KEY (`type`) REFERENCES `room_types` (`id`),
  ADD CONSTRAINT `rooms_ibfk_2` FOREIGN KEY (`floor`) REFERENCES `floors` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
