package com.xyzwps.yaff.server.controller;

import com.xyzwps.yaff.server.dto.flow.FlowSavePayload;
import com.xyzwps.yaff.server.dto.flow.NodeMetaDataView;
import com.xyzwps.yaff.server.model.entity.FlowRow;
import com.xyzwps.yaff.server.service.FlowService;
import com.xyzwps.yaff.server.yaff.Yaff;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/apis/yaff")
public class YaffController {

    private final FlowService flowService;

    @GetMapping("/metadata")
    public List<NodeMetaDataView> getMetaData() {
        return Yaff.getMetaData().stream().map(NodeMetaDataView::from).toList();
    }

    @GetMapping("/flows")
    public List<FlowRow> getFlows() {
        return flowService.findAll();
    }

    @GetMapping("/flows/{id}")
    public FlowRow getFlow(@PathVariable int id) {
        return flowService.findById(id).orElseThrow(() -> new RuntimeException("TODO: 404"));
    }

    @PostMapping("/flows")
    public FlowRow insertFlow(@RequestBody @Valid FlowSavePayload it) {
        return flowService.createFlow(it);
    }

    @PutMapping("/flows/{id}")
    public FlowRow updateFlow(@PathVariable int id, @RequestBody @Valid FlowSavePayload it) {
        return flowService.updateFlow(id, it);
    }

    @DeleteMapping("/flows/{id}")
    public void deleteFlow(@PathVariable int id) {
        flowService.deleteFlow(id);
    }

}
