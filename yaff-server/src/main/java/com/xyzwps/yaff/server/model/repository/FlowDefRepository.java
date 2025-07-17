package com.xyzwps.yaff.server.model.repository;

import com.xyzwps.yaff.server.model.entity.FlowDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowDefRepository extends JpaRepository<FlowDef, Long> {



}
