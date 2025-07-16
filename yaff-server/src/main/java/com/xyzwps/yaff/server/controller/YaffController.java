package com.xyzwps.yaff.server.controller;

import com.xyzwps.yaff.server.dto.Paged;
import com.xyzwps.yaff.server.dto.flow.FlowGetDTO;
import com.xyzwps.yaff.server.dto.flow.FlowSavePayload;
import com.xyzwps.yaff.server.dto.flow.NodeMetaDataView;
import com.xyzwps.yaff.server.model.entity.FlowRow;
import com.xyzwps.yaff.server.service.FlowService;
import com.xyzwps.yaff.server.yaff.Yaff;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Yaff")
@AllArgsConstructor
@RestController
@RequestMapping("/apis/yaff")
public class YaffController {

    private final FlowService flowService;

    @Operation(summary = "Get yaff node metadata")
    @GetMapping("/metadata")
    public List<NodeMetaDataView> getMetaData() {
        return Yaff.getMetaData().stream().map(NodeMetaDataView::from).toList();
    }

    @Operation(summary = "Get flows")
    @GetMapping("/flows")
    public Paged<FlowRow> getFlows(@Valid FlowGetDTO flowGetDTO) {
        var page = PageRequest.of(flowGetDTO.page - 1, flowGetDTO.size);
        var paged = flowService.findAll(page);
        return Paged.of(paged);
    }

    @Operation(summary = "Get flow by id")
    @GetMapping("/flows/{id}")
    public FlowRow getFlow(@Schema(description = "Flow id") @PathVariable("id") long id) {
        return flowService.findById(id).orElseThrow(() -> new RuntimeException("TODO: 404"));
    }

    @Operation(summary = "Create flow")
    @PostMapping("/flows")
    public FlowRow insertFlow(@RequestBody @Valid FlowSavePayload it) {
        return flowService.createFlow(it);
    }

    @Operation(summary = "Update flow")
    @PutMapping("/flows/{id}")
    public FlowRow updateFlow(@Schema(description = "Flow id") @PathVariable("id") long id,
                              @RequestBody @Valid FlowSavePayload it) {
        return flowService.updateFlow(id, it);
    }

    @Operation(summary = "Delete flow")
    @DeleteMapping("/flows/{id}")
    public void deleteFlow(@Schema(description = "Flow id") @PathVariable("id") long id) {
        flowService.deleteFlow(id);
    }

    @Operation(summary = "Run flow")
    @PostMapping("/flows/{id}/run")
    public Map<String, Object> insertFlow(@Schema(description = "Flow id") @PathVariable("id") long id) {
        return flowService.run(id);
    }

}
