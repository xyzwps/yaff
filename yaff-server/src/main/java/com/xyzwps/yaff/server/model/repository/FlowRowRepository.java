package com.xyzwps.yaff.server.model.repository;

import com.xyzwps.yaff.server.model.entity.FlowRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowRowRepository extends JpaRepository<FlowRow, Long> {



}
