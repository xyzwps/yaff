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

export const createFlow = async (payload: CreateFlowPayload) => {
  const response = await ky.post("/apis/yaff/flows", {
    json: {
      dedupKey: payload.dedupKey,
      description: payload.description,
      data: JSON.stringify(payload.data),
    },
  });
  return response.json();
};

export const getAllFlows = async (): Promise<FlowRow[]> => {
  const response = await ky.get(`/apis/yaff/flows`);
  return response.json();
};
