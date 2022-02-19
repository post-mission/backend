package com.postmission.repository;

import com.postmission.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    List<Ticket> findByUserId(Long userId);

    @Query(value = "select * from ticket where date_format(watched_date,'%Y-%m') = :date and user_id = :userId", nativeQuery = true)
    List<Ticket> findTicketByDate(String date, Long userId);

    @Query(value = "select date_format(watched_date, '%Y-%m') startDate from ticket where user_id=:userId and watched_date is not null order by watched_date limit 1", nativeQuery = true)
    String getStartDate(Long userId);

    @Query(value = "select date_format(watched_date, '%Y-%m') endDate from ticket where user_id=:userId and watched_date is not null order by watched_date desc limit 1", nativeQuery = true)
    String getEndDate(Long userId);

    @Query(value = "select count(*) from ticket where user_id=:userId group by name order by count(*) desc limit 1", nativeQuery = true)
    int getMostTicketCount(Long userId);

    List<Ticket> findByPrivateCheckAndWatched(boolean privateCheck, boolean watched);

    List<Ticket> findByName(String name);

}
