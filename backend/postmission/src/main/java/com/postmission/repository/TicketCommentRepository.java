package com.postmission.repository;

import com.postmission.model.TicketComments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TicketCommentRepository extends JpaRepository<TicketComments, Long> {

    List<TicketComments> findTicketCommentsByTicketIdIs(Long id);



}
