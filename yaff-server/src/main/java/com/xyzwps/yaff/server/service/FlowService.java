package com.xyzwps.yaff.server.service;


import com.xyzwps.yaff.server.dto.flow.FlowSavePayload;
import com.xyzwps.yaff.server.model.entity.FlowRow;
import com.xyzwps.yaff.server.model.repository.FlowRowRepository;
import com.xyzwps.yaff.server.yaff.Yaff;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FlowService {

    private final FlowRowRepository flowRowRepository;


    public List<FlowRow> findAll() {
        return flowRowRepository.findAll();
    }

    public Optional<FlowRow> findById(long id) {
        return flowRowRepository.findById(id);
    }

    public void deleteFlow(long id) {
        flowRowRepository.deleteById(id);
    }

    // TODO: 更细致的检查
    @Transactional
    public FlowRow updateFlow(long id, FlowSavePayload it) {
        Yaff.FACTORY.fromJSON(it.getData());
        var flow = flowRowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flow not found")); // TODO: ex
        flow.setDescription(it.getDescription());
        flow.setData(it.getData());
        flow.setUpdatedAt(LocalDateTime.now());
        return flowRowRepository.save(flow);
    }

    public FlowRow createFlow(FlowSavePayload it) {
        Yaff.FACTORY.fromJSON(it.getData());
        var row = FlowRow.builder()
                .description(it.getDescription())
                .data(it.getData())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return flowRowRepository.save(row);
    }


}
