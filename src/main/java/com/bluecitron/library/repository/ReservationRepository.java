package com.bluecitron.library.repository;

import com.bluecitron.library.entity.Book;
import com.bluecitron.library.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
