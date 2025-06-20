import ky from "ky";
import type { FlowNode } from "./components/FlowCanvas/types";

export const getMetaData = async (): Promise<NodeMetaData[]> => {
  const response = await ky.get("/apis/yaff/metadata");
  return response.json();
};

type CreateFlowPayload = {
  dedupKey: string;
  description: string;
  data: {
    flowNodes: FlowNode[];
  };
};

export const createFlow = (payload: CreateFlowPayload) =>
  ky.post("/apis/yaff/flows", {
    json: {
      dedupKey: payload.dedupKey,
      description: payload.description,
      data: JSON.stringify(payload.data),
    },
  });

type UpdateFlowPayload = {
  id: number;
  description: string;
  data: {
    flowNodes: FlowNode[];
  };
};

export const updateFlow = (payload: UpdateFlowPayload) =>
  ky.put(`/apis/yaff/flows/${payload.id}`, {
    json: {
      id: payload.id,
      description: payload.description,
      data: JSON.stringify(payload.data),
    },
  });

export const deleteFlow = (id: number) => ky.delete(`/apis/yaff/flows/${id}`);

export const getFlow = async (id: number): Promise<FlowRow> => {
  const response = await ky.get(`/apis/yaff/flows/${id}`);
  return response.json();
};

export const getAllFlows = async (): Promise<FlowRow[]> => {
  const response = await ky.get(`/apis/yaff/flows`);
  return response.json();
};
