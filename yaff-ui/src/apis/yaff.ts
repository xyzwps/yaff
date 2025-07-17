import type { FlowNode } from "@/components/FlowCanvas/types";
import type { FlowDef, NodeMetaData, Paged } from "@/types";
import { del, get, post, put } from "./http";

export const getMetaData = async (): Promise<NodeMetaData[]> =>
  get({ url: "/apis/yaff/metadata" });

type FlowSavePayload = {
  description: string;
  data: {
    flowNodes: FlowNode[];
  };
};

export const createFlow = (payload: FlowSavePayload) =>
  post({
    url: "/apis/yaff/flows",
    json: {
      description: payload.description,
      data: JSON.stringify(payload.data),
    },
  });

type UpdateFlowPayload = { id: number } & FlowSavePayload;

export const updateFlow = (payload: UpdateFlowPayload) =>
  put({
    url: `/apis/yaff/flows/${payload.id}`,
    json: {
      description: payload.description,
      data: JSON.stringify(payload.data),
    },
  });

export const deleteFlow = (id: number) =>
  del({ url: `/apis/yaff/flows/${id}` });

export const getFlow = async (id: number): Promise<FlowDef> =>
  get({ url: `/apis/yaff/flows/${id}` });

export type FlowGetDTO = {
  page: number;
  size: number;
};

export const getAllFlows = async (dto: FlowGetDTO): Promise<Paged<FlowDef>> =>
  get({ url: `/apis/yaff/flows`, qs: dto });

export const runFlow = async (
  id: number
): Promise<Record<string, unknown | null | undefined>> =>
  post({ url: `/apis/yaff/flows/${id}/run` });
