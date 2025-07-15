import ky from "ky";
import type { FlowNode } from "./components/FlowCanvas/types";
import type { FlowRow, NodeMetaData } from "./types";

export const getMetaData = async (): Promise<NodeMetaData[]> => {
  const response = await ky.get("/apis/yaff/metadata");
  return response.json();
};

type FlowSavePayload = {
  description: string;
  data: {
    flowNodes: FlowNode[];
  };
};

export const createFlow = (payload: FlowSavePayload) =>
  ky.post("/apis/yaff/flows", {
    json: {
      description: payload.description,
      data: JSON.stringify(payload.data),
    },
  });

type UpdateFlowPayload = { id: number } & FlowSavePayload;

export const updateFlow = (payload: UpdateFlowPayload) =>
  ky.put(`/apis/yaff/flows/${payload.id}`, {
    json: {
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
