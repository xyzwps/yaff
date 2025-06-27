package com.xyzwps.libs.demo.resource;

import com.xyzwps.libs.demo.commons.Yaff;
import com.xyzwps.libs.demo.dto.CreateFlowPayload;
import com.xyzwps.libs.demo.dto.UpdateFlowPayload;
import com.xyzwps.libs.demo.entity.FlowInfo;
import com.xyzwps.libs.yaff.NodeMetaData;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "Yaff")
@Path("/apis/yaff")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class YaffResource {


    @Path("/metaData")
    @GET
    public List<NodeMetaData> getMetaData() {
        return Yaff.getMetaData();
    }

    @Path("/flows")
    @GET
    public List<FlowInfo> getAll() {
        return FlowInfo.listAll();
    }

    @Path("/flows/{id}")
    @GET
    public Optional<FlowInfo> get(Integer id) {
        return FlowInfo.findByIdOptional(id);
    }

    @Path("/flows")
    @POST
    public void create(@Valid CreateFlowPayload it) {
        var entity = it.toEntity();
        Yaff.FACTORY.fromJSON(entity.getData());
        entity.persistAndFlush();
    }

    @Path("/flows")
    @PUT
    @Transactional
    public void update(@Valid UpdateFlowPayload it) {
        FlowInfo flow = FlowInfo.findById(it.getId());
        if (flow == null) {
            return;
        }

        Yaff.FACTORY.fromJSON(it.getData());

        flow.setDescription(it.getDescription());
        flow.setData(it.getData());
        flow.setUpdatedAt(LocalDateTime.now());
        flow.persistAndFlush();
    }

    @Path("/flows/{id}")
    @DELETE
    @Transactional
    public void delete(@PathParam("id") Integer id) {
        FlowInfo.deleteById(id);
    }
}
