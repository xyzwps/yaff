package com.xyzwps.yaff.server.service;


import com.xyzwps.yaff.server.dto.flow.FlowSavePayload;
import com.xyzwps.yaff.server.model.entity.FlowDef;
import com.xyzwps.yaff.server.model.repository.FlowDefRepository;
import com.xyzwps.yaff.server.yaff.Yaff;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FlowService {

    private final FlowDefRepository flowDefRepository;

    public Page<FlowDef> findAll(Pageable page) {
        return flowDefRepository.findAll(page);
    }

    public Optional<FlowDef> findById(long id) {
        return flowDefRepository.findById(id);
    }

    public void deleteFlow(long id) {
        flowDefRepository.deleteById(id);
    }

    // TODO: 更细致的检查
    @Transactional
    public FlowDef updateFlow(long id, FlowSavePayload it) {
        Yaff.fromJSON(it.getData());
        var flow = flowDefRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flow not found")); // TODO: ex
        flow.setDescription(it.getDescription());
        flow.setData(it.getData());
        flow.setUpdatedAt(LocalDateTime.now());
        return flowDefRepository.save(flow);
    }

    public FlowDef createFlow(FlowSavePayload it) {
        Yaff.fromJSON(it.getData());
        var row = FlowDef.builder()
                .description(it.getDescription())
                .data(it.getData())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return flowDefRepository.save(row);
    }

    public Map<String, Object> run(long id) {
        return flowDefRepository.findById(id)
                .map(this::run)
                .orElseThrow(() -> new RuntimeException("Flow not found")); // TODO: ex
    }

    public Map<String, Object> run(@NonNull FlowDef flowDef) {
        var flow = Yaff.fromJSON(flowDef.getData());
        return Yaff.execute(flow).toMap();
    }
}
