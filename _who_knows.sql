-- phpMyAdmin SQL Dump
-- version 4.9.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Waktu pembuatan: 19 Feb 2021 pada 15.55
-- Versi server: 5.7.26
-- Versi PHP: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `_who_knows`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `_table_participant`
--

CREATE TABLE `_table_participant` (
  `participantId` varchar(255) NOT NULL,
  `roomId` varchar(255) NOT NULL,
  `userId` varchar(255) NOT NULL,
  `totalQuiz` int(255) DEFAULT NULL,
  `totalTime` int(255) DEFAULT NULL,
  `currentPage` varchar(255) DEFAULT NULL,
  `timeRemaining` varchar(255) DEFAULT NULL,
  `expired` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `_table_participant`
--

INSERT INTO `_table_participant` (`participantId`, `roomId`, `userId`, `totalQuiz`, `totalTime`, `currentPage`, `timeRemaining`, `expired`) VALUES
('19ccb021-36f6-458c-a1fd-e47c052c3d13', '6fcb5d7e-a85b-40c5-894c-874977982c54', '6b05f8aa-6d0c-47ac-9d2f-46eb99e51585', 0, 34, '0', '34', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `_table_quiz`
--

CREATE TABLE `_table_quiz` (
  `quizId` varchar(255) NOT NULL,
  `roomId` varchar(255) NOT NULL,
  `question` varchar(255) DEFAULT NULL,
  `imageUrl` varchar(255) DEFAULT NULL,
  `optA` varchar(255) DEFAULT NULL,
  `optB` varchar(255) DEFAULT NULL,
  `optC` varchar(255) DEFAULT NULL,
  `optD` varchar(255) DEFAULT NULL,
  `optE` varchar(255) DEFAULT NULL,
  `answer` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `_table_quiz`
--

INSERT INTO `_table_quiz` (`quizId`, `roomId`, `question`, `imageUrl`, `optA`, `optB`, `optC`, `optD`, `optE`, `answer`) VALUES
('40da15c1-f455-4049-b8ef-1762cc2dac4b', '6fcb5d7e-a85b-40c5-894c-874977982c54', 'Apakah nama dari satelit alami bumi kita?', 'http://image_url.com/image1.png', 'Balon', 'Burung', 'Bulan', 'Barang', 'Bulat', 'C'),
('98d25726-7d5e-465f-a7c2-b5c32bc0ac53', '6fcb5d7e-a85b-40c5-894c-874977982c54', 'Berapakah jumlah planet didalam tata surya kita di tahun 2020', 'http://image_url.com/image1.png', '1 planet', '2 planet', '21 planet', '31 planet', '12 planet', 'E'),
('a0b626a2-bba0-4b75-8689-afc69696381b', '6fcb5d7e-a85b-40c5-894c-874977982c54', 'Siapa manusia pertama yang pernah mendarat dibulan menurut NASA?', 'http://image_url.com/image1.png', 'Luis amstrong', 'Andromeda', 'Bima sakti', 'Soekarno', 'Telkomsel', 'A'),
('c68b1507-c18f-4fc2-9099-65815e7dabe2', '6fcb5d7e-a85b-40c5-894c-874977982c54', 'Kapan nabi Muhammad S.A.W lahir?', 'http://image_url.com/image1.png', 'Ramadhan', 'Januari', 'Desember bukan bulan kelahiran beliau', 'Oktober', 'April', 'C');

-- --------------------------------------------------------

--
-- Struktur dari tabel `_table_result`
--

CREATE TABLE `_table_result` (
  `resultId` varchar(255) NOT NULL,
  `roomId` varchar(255) NOT NULL,
  `userId` varchar(255) NOT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `correctQuiz` varchar(255) DEFAULT NULL,
  `wrongQuiz` varchar(255) DEFAULT NULL,
  `score` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `_table_result`
--

INSERT INTO `_table_result` (`resultId`, `roomId`, `userId`, `userName`, `correctQuiz`, `wrongQuiz`, `score`) VALUES
('cecfc07e-6c20-43db-9f06-4ff5a656b890', '6fcb5d7e-a85b-40c5-894c-874977982c54', '6b05f8aa-6d0c-47ac-9d2f-46eb99e51585', 'Utif Milkedori', '[Apakah nama dari satelit alami bumi kita?, Berapakah jumlah planet didalam tata surya kita di tahun 2020, Siapa manusia pertama yang pernah mendarat dibulan menurut NASA?, Kapan nabi Muhammad S.A.W lahir?]', '[Apakah nama dari satelit alami bumi kita?]', '4 * 10 / 4 = 10');

-- --------------------------------------------------------

--
-- Struktur dari tabel `_table_room`
--

CREATE TABLE `_table_room` (
  `roomId` varchar(255) NOT NULL,
  `userId` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `expired` tinyint(1) DEFAULT NULL,
  `minute` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `_table_room`
--

INSERT INTO `_table_room` (`roomId`, `userId`, `title`, `desc`, `expired`, `minute`) VALUES
('6fcb5d7e-a85b-40c5-894c-874977982c53', '6b05f8aa-6d0c-47ac-9d2f-46eb99e51585', 'Unexpected', 'Unexpected Unexpected Unexpected', 0, '23'),
('6fcb5d7e-a85b-40c5-894c-874977982c54', '6b05f8aa-6d0c-47ac-9d2f-46eb99e51585', 'Ontology', 'Perjalanan hidup seseorang tiada yang tahu', 0, '34');

-- --------------------------------------------------------

--
-- Struktur dari tabel `_table_user`
--

CREATE TABLE `_table_user` (
  `userId` varchar(255) NOT NULL,
  `fullName` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data untuk tabel `_table_user`
--

INSERT INTO `_table_user` (`userId`, `fullName`, `email`, `phone`, `userName`, `password`) VALUES
('58eb70f4-0e83-4bf0-aac5-c40f9bad3565', 'Utif Milkedori', 'utifmd@gmail.com', '081275340004', 'utifmd', '9809poiiop'),
('6b05f8aa-6d0c-47ac-9d2f-46eb99e51585', 'Lebok Lombok', 'lebok@gmail.com', '081212121212', 'lebok', '121212');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `_table_participant`
--
ALTER TABLE `_table_participant`
  ADD PRIMARY KEY (`participantId`,`roomId`,`userId`);

--
-- Indeks untuk tabel `_table_quiz`
--
ALTER TABLE `_table_quiz`
  ADD PRIMARY KEY (`quizId`,`roomId`) USING BTREE;

--
-- Indeks untuk tabel `_table_result`
--
ALTER TABLE `_table_result`
  ADD PRIMARY KEY (`resultId`,`roomId`,`userId`);

--
-- Indeks untuk tabel `_table_room`
--
ALTER TABLE `_table_room`
  ADD PRIMARY KEY (`roomId`,`userId`) USING BTREE;

--
-- Indeks untuk tabel `_table_user`
--
ALTER TABLE `_table_user`
  ADD PRIMARY KEY (`userId`);